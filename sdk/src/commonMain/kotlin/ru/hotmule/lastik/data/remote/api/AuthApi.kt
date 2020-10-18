package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.entities.SessionResponse

class AuthApi(
    private val client: HttpClient,
    private val prefs: PrefsStore,
    private val apiKey: String,
    private val secret: String
) {

    private fun HttpRequestBuilder.authApi(
        method: String
    ) {
        api("auth", method, apiKey, prefs.token, secret)
    }

    suspend fun getSession() = client.get<SessionResponse?> {
        authApi("getSession")
    }
}