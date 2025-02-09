package com.raphaelfavero.stylearena.ranking

import com.raphaelfavero.stylearena.core.network.SessionManager
import com.raphaelfavero.stylearena.core.network.Style
import com.raphaelfavero.stylearena.core.network.StyleArenaService
import javax.inject.Inject

interface DailyWinnersRepository {
    suspend fun getDailyWinners(): Triple<Style, Style, Style>?

    class Impl @Inject constructor(
        private val api: StyleArenaService,
        private val session: SessionManager
    ) : DailyWinnersRepository {

        override suspend fun getDailyWinners(): Triple<Style, Style, Style>? {
            return try {
                val response = api.getDailyWinners(session.sessionId)

                if (response.isSuccessful) {
                    val body = response.body()!!
                    Triple(body.rank1, body.rank2, body.rank3)
                } else {
                    null
                }

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}