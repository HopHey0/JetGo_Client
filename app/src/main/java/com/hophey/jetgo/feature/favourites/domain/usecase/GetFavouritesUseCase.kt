package com.hophey.jetgo.feature.favourites.domain.usecase

import com.hophey.jetgo.feature.favourites.domain.repository.FavouritesRepository

class GetFavouritesUseCase(private val repo: FavouritesRepository) {

    operator fun invoke() = repo.favourites

    fun ids() = repo.favouriteIds
}