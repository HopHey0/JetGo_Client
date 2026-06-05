package com.hophey.jetgo.feature.searchFlights.domain.model

data class Airport(
    val id: Int,
    val fullName: String,
    val code: String,
    val cityName: String
)

data class City(
    val name: String,
    val airports: List<Airport>
)

data class CitySearchResult(
    val cities: List<City>
)