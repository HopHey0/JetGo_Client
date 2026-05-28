package com.hophey.jetgo.feature.searchFlights.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class FlightDto(
    val id: Long,
    val flightNumber: String,
    val price: Double,
    val departureTime: String,
    val departureAirport: String,
    val departureCity: String,
    val departureUtcDiff: Int,
    val arrivalTime: String,
    val arrivalAirport: String,
    val arrivalCity: String,
    val arrivalCountry: String,
    val arrivalUtcDiff: Int,
    val airlineName: String,
    val airlineCode: String,
    val airlineLogo: String,
    val discountRate: Double = 0.0
)

@Serializable
data class OffersResponse(
    val offers: List<FlightDto>
)