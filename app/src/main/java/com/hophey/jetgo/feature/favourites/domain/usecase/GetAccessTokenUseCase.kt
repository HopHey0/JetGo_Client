package com.hophey.jetgo.feature.favourites.domain.usecase

import com.hophey.jetgo.feature.favourites.domain.repository.TokenRepository

class GetAccessTokenUseCase(
    private val repo: TokenRepository
) {
    suspend operator fun invoke(): String? = repo.getAccessToken()
}