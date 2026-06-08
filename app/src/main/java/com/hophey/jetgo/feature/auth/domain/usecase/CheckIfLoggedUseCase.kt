package com.hophey.jetgo.feature.auth.domain.usecase

import com.hophey.jetgo.feature.auth.domain.repository.AuthRepository

class CheckIfLoggedUseCase(
    private val repo: AuthRepository
) {
    operator fun invoke() = repo.isLogged
}