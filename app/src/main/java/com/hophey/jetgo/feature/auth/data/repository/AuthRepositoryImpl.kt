package com.hophey.jetgo.feature.auth.data.repository

import com.hophey.jetgo.core.datastorage.SessionStorage
import com.hophey.jetgo.feature.auth.data.api.AuthApi
import com.hophey.jetgo.feature.auth.data.dto.AuthRequest
import com.hophey.jetgo.feature.auth.data.dto.LogoutRequest
import com.hophey.jetgo.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlin.text.clear

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val sessionStorage: SessionStorage
) : AuthRepository {
    override val isLogged: Flow<Boolean> = sessionStorage.isLoggedIn

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val response = authApi.login(AuthRequest(email, password))
        sessionStorage.saveTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val refresh = sessionStorage.getRefreshToken() ?: return@runCatching
        authApi.logout(LogoutRequest(refresh))
        sessionStorage.clear()
    }

    override suspend fun register(email: String, password: String): Result<Unit> = runCatching {
            val response = authApi.register(AuthRequest(email, password))
            sessionStorage.saveTokens(response.accessToken, response.refreshToken)
        }
}