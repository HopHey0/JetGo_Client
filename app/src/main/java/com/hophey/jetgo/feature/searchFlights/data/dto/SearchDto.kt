package com.hophey.jetgo.feature.searchFlights.data.dto

import com.hophey.jetgo.feature.searchFlights.domain.model.RecentSearch
import kotlinx.serialization.Serializable

@Serializable
data class RecentSearchDto(
    val departureCode: String,
    val departureCity: String,
    val arrivalCode: String,
    val arrivalCity: String,
    val date: String,
    val passengers: Int
)

fun RecentSearchDto.toDomain() = RecentSearch(
    departureCode = this.departureCode,
    departureCity = this.departureCity,
    arrivalCode = this.arrivalCode,
    arrivalCity = this.arrivalCity,
    date = this.date,
    passengers = this.passengers
)