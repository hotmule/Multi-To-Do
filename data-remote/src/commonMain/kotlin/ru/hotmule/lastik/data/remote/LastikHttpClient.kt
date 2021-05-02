package ru.hotmule.lastik.data.remote

import co.touchlab.kermit.Kermit
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.AuthApi
import ru.hotmule.lastik.data.remote.api.TrackApi
import ru.hotmule.lastik.data.remote.api.UserApi

class LastikHttpClient(
    prefs: PrefsStore
) {

    private val kermit = Kermit()
    private val config = LastikHttpClientConfig()

    private val client = HttpClient(config.engine) {

        expectSuccess = false

        if (config.loggingEnabled) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        kermit.d("Ktor") {
                            message
                        }
                    }
                }
            }
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(
                kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        BrowserUserAgent()

        HttpResponseValidator {
            validateResponse {

                if (!it.status.isSuccess()) {
                    when (it.status) {
                        HttpStatusCode.Unauthorized -> prefs.clear()
                        else -> throwErrorWithMessage(it)
                    }
                }

                handleResponseException { throwable ->
                    error(throwable.message ?: "Unknown Error")
                }
            }
        }
    }

    private suspend fun throwErrorWithMessage(it: HttpResponse) {

        var errorMessage = "Unknown error"

        it.content.readUTF8Line()?.let { error ->
            Json.parseToJsonElement(error)
                .jsonObject["message"]
                ?.jsonPrimitive
                ?.contentOrNull
                ?.let { message -> errorMessage = message }
        }

        error(errorMessage)
    }

    companion object {
        const val defaultImageUrl = "https://lastfm.freetls.fastly.net/i/u/64s/" +
                "2a96cbd8b46e442fc41c2b86b821562f.png"
    }

    val authApi = AuthApi(client, config.apiKey, config.secret)
    val trackApi = TrackApi(client, prefs, config.apiKey, config.secret)
    val userApi = UserApi(client, prefs, config.apiKey)
}