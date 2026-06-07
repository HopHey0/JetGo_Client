package com.hophey.jetgo.feature.searchFlights.data.repository

import com.hophey.jetgo.feature.searchFlights.data.api.FlightApi
import com.hophey.jetgo.feature.searchFlights.data.dto.FlightsRequest
import com.hophey.jetgo.feature.searchFlights.data.dto.OffersResponse
import com.hophey.jetgo.feature.searchFlights.domain.repository.FlightRepository
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FlightSearchParams

class FlightRepositoryImpl(private val api: FlightApi) : FlightRepository {

    override suspend fun getHotOffers(): Result<OffersResponse> = runCatching {
        api.getHotOffers()
    }

    override suspend fun searchFlights(params: FlightSearchParams): Result<OffersResponse> = runCatching {
        api.searchFlights(
            FlightsRequest(
                departureCity = params.departureCity,
                arrivalCity = params.arrivalCity,
                departureDate = params.departureDate.toString(),
                personAmount = params.passengers
            )
        )
    }
}