package com.hophey.jetgo.feature.searchFlights.domain.repository

import com.hophey.jetgo.feature.searchFlights.data.api.FlightApi
import com.hophey.jetgo.feature.searchFlights.data.dto.OffersResponse

abstract class FlightRepository(
    private val api: FlightApi
){
    abstract suspend fun getHotOffers(): Result<OffersResponse>
}