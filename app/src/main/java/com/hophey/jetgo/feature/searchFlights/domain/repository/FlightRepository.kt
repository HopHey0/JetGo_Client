package com.hophey.jetgo.feature.searchFlights.domain.repository

import com.hophey.jetgo.feature.searchFlights.data.dto.OffersResponse
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FlightSearchParams

interface FlightRepository {

    suspend fun getHotOffers(): Result<OffersResponse>

    suspend fun searchFlights(params: FlightSearchParams): Result<OffersResponse>
}