package com.hophey.jetgo.core.network

import com.hophey.jetgo.core.datastorage.SessionStorage
import com.hophey.jetgo.feature.auth.data.dto.AuthResponse
import com.hophey.jetgo.feature.auth.data.dto.RefreshTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(sessionStorage: SessionStorage): HttpClient {
        return HttpClient(Android) {

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                })
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val access  = sessionStorage.getAccessToken()  ?: return@loadTokens null
                        val refresh = sessionStorage.getRefreshToken() ?: return@loadTokens null
                        BearerTokens(access, refresh)
                    }
                    refreshTokens {
                        val refresh = sessionStorage.getRefreshToken()
                            ?: return@refreshTokens null

                        val response: AuthResponse =
                            client.post("/auth/refresh") {
                                markAsRefreshTokenRequest()
                                setBody(RefreshTokenRequest(refresh))
                            }.body()

                        sessionStorage.saveTokens(
                            response.accessToken,
                            response.refreshToken
                        )

                        BearerTokens(
                            response.accessToken,
                            response.refreshToken
                        )
                    }
                }
            }

            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.BODY
            }

            install(HttpTimeout) {
                requestTimeoutMillis  = 15_000L
                connectTimeoutMillis  = 15_000L
                socketTimeoutMillis   = 15_000L
            }

            defaultRequest {
                url(ApiConfig.BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }
}