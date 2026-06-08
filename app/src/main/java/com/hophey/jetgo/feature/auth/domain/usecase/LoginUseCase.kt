package com.hophey.jetgo.feature.auth.domain.usecase


import com.hophey.jetgo.feature.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> = repo.login(email, password)
}