package com.hophey.jetgo.feature.favourites.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hophey.jetgo.feature.searchFlights.domain.model.Flight

@Entity(tableName = "favourite_flights")
data class FavouriteFlightEntity(
    @PrimaryKey val flightId: Long,
    val flightNum: String,
    val price: Int,
    val departureCity: String,
    val departureAirport: String,
    val departureTime: String,
    val departureDate: String,
    val arrivalCity: String,
    val arrivalAirport: String,
    val arrivalTime: String,
    val arrivalDate: String,
    val arrivalCountry: String,
    val airlineName: String,
    val airlineLogo: String,
    val timeTravel: String
)


fun FavouriteFlightEntity.toDomain() = Flight(
    id = this.flightId,
    price = this.price,
    flightNum = this.flightNum,
    departureDate = this.departureDate,
    departureTime = this.departureTime,
    departureAirport = this.departureAirport,
    departureCity = this.departureCity,
    arrivalTime = this.arrivalTime,
    arrivalDate = this.arrivalDate,
    arrivalAirport = this.arrivalAirport,
    arrivalCity = this.arrivalCity,
    arrivalCountry = this.arrivalCountry,
    airlineName = this.airlineName,
    airlineLogo = this.airlineLogo,
    timeTravel = this.timeTravel,
    priceWithDiscount = 0,
    discountRate = 0
)

fun Flight.toEntity() = FavouriteFlightEntity(
    flightId = this.id,
    price = this.price,
    flightNum = this.flightNum,
    departureDate = this.departureDate,
    departureTime = this.departureTime,
    departureAirport = this.departureAirport,
    departureCity = this.departureCity,
    arrivalTime = this.arrivalTime,
    arrivalDate = this.arrivalDate,
    arrivalAirport = this.arrivalAirport,
    arrivalCity = this.arrivalCity,
    arrivalCountry = this.arrivalCountry,
    airlineName = this.airlineName,
    airlineLogo = this.airlineLogo,
    timeTravel = this.timeTravel,
)