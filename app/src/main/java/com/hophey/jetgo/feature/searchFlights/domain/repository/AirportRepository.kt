package com.hophey.jetgo.feature.searchFlights.domain.repository

import com.hophey.jetgo.feature.searchFlights.domain.model.CitySearchResult

interface AirportRepository {
    suspend fun searchAirports(query: String): Result<CitySearchResult>
}