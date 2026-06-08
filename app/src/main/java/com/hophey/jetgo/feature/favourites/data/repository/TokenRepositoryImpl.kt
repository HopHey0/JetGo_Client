package com.hophey.jetgo.feature.favourites.data.repository

import com.hophey.jetgo.core.datastorage.SessionStorage
import com.hophey.jetgo.feature.favourites.domain.repository.TokenRepository

class TokenRepositoryImpl(
    private val tokenStorage: SessionStorage
) : TokenRepository{
    override suspend fun getAccessToken(): String? = tokenStorage.getAccessToken()
}