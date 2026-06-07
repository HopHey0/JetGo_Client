package com.hophey.jetgo.feature.searchFlights.domain.usecase

import com.hophey.jetgo.feature.searchFlights.domain.model.Flight
import com.hophey.jetgo.feature.searchFlights.domain.model.toDomain
import com.hophey.jetgo.feature.searchFlights.domain.repository.FlightRepository
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FlightSearchParams

class SearchFlightsUseCase(
    private val flightRepository: FlightRepository
) {
    suspend operator fun invoke(params: FlightSearchParams): Result<List<Flight>> = runCatching {
        flightRepository.searchFlights(params)
            .getOrThrow()
            .offers
            .map { it.toDomain() }
    }
}