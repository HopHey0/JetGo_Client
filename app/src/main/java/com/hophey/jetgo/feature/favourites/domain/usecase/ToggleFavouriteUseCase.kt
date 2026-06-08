package com.hophey.jetgo.feature.favourites.domain.usecase


import com.hophey.jetgo.feature.favourites.domain.repository.FavouritesRepository
import com.hophey.jetgo.feature.searchFlights.domain.model.Flight

class ToggleFavouriteUseCase(private val repo: FavouritesRepository) {

    suspend operator fun invoke(flight: Flight, isLoggedIn: Boolean) = repo.toggle(flight, isLoggedIn)
}