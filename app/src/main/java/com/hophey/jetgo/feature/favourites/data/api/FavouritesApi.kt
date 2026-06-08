package com.hophey.jetgo.feature.favourites.data.api

import com.hophey.jetgo.feature.searchFlights.data.dto.OffersResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post

class FavouritesApi(private val client: HttpClient) {
    suspend fun getFavourites(): OffersResponse =
        client.get("/favourites").body()

    suspend fun addFavourite(flightId: Long) =
        client.post("/favourites/$flightId")

    suspend fun removeFavourite(flightId: Long) =
        client.delete("/favourites/$flightId")
}