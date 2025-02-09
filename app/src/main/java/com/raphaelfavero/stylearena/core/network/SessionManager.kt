package com.raphaelfavero.stylearena.core.network

import java.util.UUID

interface SessionManager {
    val sessionId: String
}

class InMemorySessionManager : SessionManager {
    override val sessionId: String = generateSessionId()

    private fun generateSessionId(): String {
        return UUID.randomUUID().toString()
    }
}