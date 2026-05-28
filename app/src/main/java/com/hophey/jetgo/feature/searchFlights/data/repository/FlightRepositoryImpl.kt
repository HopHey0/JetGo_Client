package com.hophey.jetgo.feature.searchFlights.data.repository

import com.hophey.jetgo.feature.searchFlights.data.api.FlightApi
import com.hophey.jetgo.feature.searchFlights.data.dto.OffersResponse
import com.hophey.jetgo.feature.searchFlights.domain.repository.FlightRepository

class FlightRepositoryImpl(private val api: FlightApi) : FlightRepository(api) {
    override suspend fun getHotOffers(): Result<OffersResponse> = runCatching {
        api.getHotOffers()
    }
}