package com.hophey.jetgo.feature.searchFlights.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AirportDto(
    val id: Int,
    val city: String,
    val fullName: String,
    val airportCode: String,
    val countryCode: String,
    val countryName: String
)

@Serializable
data class AirportsResponse(
    val airports: List<AirportDto>
)