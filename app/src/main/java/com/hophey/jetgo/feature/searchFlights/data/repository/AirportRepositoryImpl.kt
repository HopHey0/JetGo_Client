package com.hophey.jetgo.feature.searchFlights.data.repository

import com.hophey.jetgo.feature.searchFlights.data.api.AirportApi
import com.hophey.jetgo.feature.searchFlights.domain.model.Airport
import com.hophey.jetgo.feature.searchFlights.domain.model.City
import com.hophey.jetgo.feature.searchFlights.domain.model.CitySearchResult
import com.hophey.jetgo.feature.searchFlights.domain.repository.AirportRepository

class AirportRepositoryImpl(
    private val api: AirportApi
) : AirportRepository {

    override suspend fun searchAirports(query: String): Result<CitySearchResult> =
        runCatching {
            val response = api.getAirportsSearch(query).airports
            val cities = response
                .groupBy { it.city }
                .map { (cityName, airports) ->
                    City(
                        name = cityName,
                        airports = airports.map { dto ->
                            Airport(
                                id = dto.id,
                                code = dto.airportCode,
                                fullName = dto.fullName,
                                cityName = dto.city
                            )
                        }
                    )
                }
            CitySearchResult(cities = cities)
        }
}