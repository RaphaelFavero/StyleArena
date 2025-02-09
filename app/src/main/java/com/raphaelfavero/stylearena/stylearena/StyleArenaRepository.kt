package com.raphaelfavero.stylearena.stylearena

import com.raphaelfavero.stylearena.core.network.SessionManager
import com.raphaelfavero.stylearena.core.network.Style
import com.raphaelfavero.stylearena.core.network.StyleArenaService
import com.raphaelfavero.stylearena.core.network.VoteRequest
import javax.inject.Inject

interface StyleArenaRepository {
    suspend fun getStylePair(): Pair<Style, Style>?
    suspend fun vote(styleId: String): Boolean

    class Impl @Inject constructor(
        private val api: StyleArenaService,
        private val session: SessionManager
    ) : StyleArenaRepository {

        override suspend fun getStylePair(): Pair<Style, Style>? {
            return try {
                val response = api.getStylePair(session.sessionId)

                if (response.isSuccessful) {
                    val body = response.body()!!
                    Pair(body.style1, body.style2)
                } else {
                    null
                }

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override suspend fun vote(styleId: String): Boolean {
            return try {
                val response = api.voteForStyle(
                    VoteRequest(
                        voteId = styleId,
                        sessionId = session.sessionId
                    )
                )
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}