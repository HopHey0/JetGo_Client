package com.hophey.jetgo.core.network

import androidx.compose.ui.autofill.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(): HttpClient {
        return HttpClient(Android) {

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                })
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
                contentType(Json)
            }
        }
    }
}