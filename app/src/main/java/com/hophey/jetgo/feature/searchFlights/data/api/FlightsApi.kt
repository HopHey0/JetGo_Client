package com.hophey.jetgo.feature.searchFlights.data.api

import com.hophey.jetgo.feature.searchFlights.data.dto.OffersResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class FlightApi(
    private val client: HttpClient
) {

    suspend fun getHotOffers(): OffersResponse {
        return client.get("/flights/hotOffers").body()
    }
}