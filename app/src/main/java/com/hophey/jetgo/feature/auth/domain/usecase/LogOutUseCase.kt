package com.hophey.jetgo.feature.auth.domain.usecase

import com.hophey.jetgo.feature.auth.domain.repository.AuthRepository

class LogoutUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> = repo.logout()
}