package com.hophey.jetgo.feature.searchFlights.domain.model

import com.hophey.jetgo.feature.searchFlights.data.dto.RecentSearchDto

data class RecentSearch(
    val departureCode: String,
    val departureCity: String,
    val arrivalCode: String,
    val arrivalCity: String,
    val date: String,
    val passengers: Int
)

fun RecentSearch.toDto() = RecentSearchDto(
    departureCode = this.departureCode,
    departureCity = this.departureCity,
    arrivalCode = this.arrivalCode,
    arrivalCity = this.arrivalCity,
    date = this.date,
    passengers = this.passengers
)