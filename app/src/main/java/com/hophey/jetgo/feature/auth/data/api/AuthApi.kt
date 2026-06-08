package com.hophey.jetgo.feature.auth.data.api

import com.hophey.jetgo.feature.auth.data.dto.AuthRequest
import com.hophey.jetgo.feature.auth.data.dto.AuthResponse
import com.hophey.jetgo.feature.auth.data.dto.LogoutRequest
import com.hophey.jetgo.feature.auth.data.dto.RefreshTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApi(private val client: HttpClient) {
    suspend fun login(request: AuthRequest): AuthResponse =
        client.post("/auth/login") {
            setBody(request)
        }.body()

    suspend fun register(request: AuthRequest): AuthResponse =
        client.post("/auth/register") {
            setBody(request)
        }.body()

    suspend fun logout(request: LogoutRequest) =
        client.post("/auth/logout") {
            setBody(request)
        }

    suspend fun refresh(request: RefreshTokenRequest): AuthResponse =
        client.post("/auth/refresh") {
            setBody(request)
        }.body()
}