package com.hophey.jetgo.feature.searchFlights.domain.model

import com.hophey.jetgo.feature.searchFlights.data.dto.FlightDto
import com.hophey.jetgo.utils.getTimeDiffInHoursFormatted
import com.hophey.jetgo.utils.toAirportLocalTime
import com.hophey.jetgo.utils.toDayAndMonth

data class Flight(
    val id: Long,
    val price: Int,
    val flightNum: String,
    val priceWithDiscount: Int,
    val departureDate: String,
    val departureTime: String,
    val departureAirport: String,
    val departureCity: String,
    val arrivalTime: String,
    val arrivalDate: String,
    val arrivalAirport: String,
    val arrivalCity: String,
    val arrivalCountry: String,
    val airlineName: String,
    val airlineLogo: String,
    val discountRate: Int,
    val timeTravel: String
)

fun FlightDto.toDomain() = Flight(
    id = this.id,
    price = this.price.toInt(),
    flightNum = this.flightNumber,
    priceWithDiscount = (this.price * (1 - (this.discountRate))).toInt(),
    departureDate = this.departureTime.toDayAndMonth(departureUtcDiff),
    departureTime = this.departureTime.toAirportLocalTime(this.departureUtcDiff),
    departureAirport = this.departureAirport,
    departureCity = this.departureCity,
    arrivalTime = this.arrivalTime.toAirportLocalTime(this.arrivalUtcDiff),
    arrivalDate = this.arrivalTime.toDayAndMonth(arrivalUtcDiff),
    arrivalAirport = this.arrivalAirport,
    arrivalCity = this.arrivalCity,
    arrivalCountry = this.arrivalCountry,
    airlineName = this.airlineName,
    airlineLogo = this.airlineLogo,
    discountRate = (this.discountRate * 100).toInt(),
    timeTravel = this.arrivalTime.getTimeDiffInHoursFormatted(this.departureTime)
)