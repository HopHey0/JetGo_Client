package com.hophey.jetgo.feature.favourites.domain.repository

import com.hophey.jetgo.feature.searchFlights.domain.model.Flight
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {

    val favourites: Flow<List<Flight>>

    val favouriteIds: Flow<List<Long>>

    suspend fun toggle(flight: Flight, isLoggedIn: Boolean): Result<Unit>

    suspend fun syncFromServer()
}