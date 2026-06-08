package com.hophey.jetgo.feature.favourites.domain.repository

interface TokenRepository{
    suspend fun getAccessToken(): String?
}