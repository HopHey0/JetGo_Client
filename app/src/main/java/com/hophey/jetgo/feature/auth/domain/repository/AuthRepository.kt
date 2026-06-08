package com.hophey.jetgo.feature.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLogged: Flow<Boolean>

    suspend fun login(email: String, password: String): Result<Unit>

    suspend fun logout(): Result<Unit>

    suspend fun register(email: String, password: String): Result<Unit>
}