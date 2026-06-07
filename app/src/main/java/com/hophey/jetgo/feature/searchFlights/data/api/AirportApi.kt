package com.hophey.jetgo.feature.searchFlights.data.api

import com.hophey.jetgo.feature.searchFlights.data.dto.AirportsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class AirportApi(
    private val client: HttpClient
) {
    suspend fun getAirportsSearch(query: String): AirportsResponse {
        return client
            .get("/flights/airports/search") { parameter("q", query) }
            .body()
    }
}