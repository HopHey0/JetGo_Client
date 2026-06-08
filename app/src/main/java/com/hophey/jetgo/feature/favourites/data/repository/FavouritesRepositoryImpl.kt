package com.hophey.jetgo.feature.favourites.data.repository

import com.hophey.jetgo.feature.favourites.data.api.FavouritesApi
import com.hophey.jetgo.feature.favourites.data.local.dao.FavouriteFlightDao
import com.hophey.jetgo.feature.favourites.data.local.entity.toDomain
import com.hophey.jetgo.feature.favourites.data.local.entity.toEntity
import com.hophey.jetgo.feature.favourites.domain.repository.FavouritesRepository
import com.hophey.jetgo.feature.searchFlights.data.dto.toDomain
import com.hophey.jetgo.feature.searchFlights.domain.model.Flight
import com.hophey.jetgo.feature.searchFlights.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FavouriteRepositoryImpl(
    private val dao: FavouriteFlightDao,
    private val api: FavouritesApi
) : FavouritesRepository {

    override val favourites: Flow<List<Flight>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override val favouriteIds: Flow<List<Long>> = dao.getAllIds()

    override suspend fun toggle(flight: Flight, isLoggedIn: Boolean): Result<Unit> = runCatching {
        val ids = dao.getAllIds().first()
        if (flight.id in ids) {
            dao.delete(flight.id)
            if (isLoggedIn) api.removeFavourite(flight.id)
        } else {
            dao.insert(flight.toEntity())
            if (isLoggedIn) api.addFavourite(flight.id)
        }
    }

    override suspend fun syncFromServer(): Unit = runCatching {
        val serverFlights = api.getFavourites().offers.map { it.toDomain() }
        serverFlights.forEach { dao.insert(it.toEntity()) }
    }.getOrElse { }
}