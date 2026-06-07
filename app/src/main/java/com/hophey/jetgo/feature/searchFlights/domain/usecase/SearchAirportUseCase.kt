package com.hophey.jetgo.feature.searchFlights.domain.usecase

import com.hophey.jetgo.feature.searchFlights.domain.model.CitySearchResult
import com.hophey.jetgo.feature.searchFlights.domain.repository.AirportRepository


class SearchAirportsUseCase(
    private val repository: AirportRepository
) {
    suspend operator fun invoke(query: String): Result<CitySearchResult>? {
        if (query.trim().length < 2) return null
        return repository.searchAirports(query.trim())
    }
}