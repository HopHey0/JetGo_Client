package com.hophey.jetgo.feature.searchFlights.domain.model

data class PassengerCount(
    val adults: Int = 1,
    val children: Int = 0
) {
    val total: Int get() = adults + children

    fun incrementAdults() = copy(adults = (adults + 1).coerceAtMost(9))
    fun decrementAdults() = copy(adults = (adults - 1).coerceAtLeast(1))
    fun incrementChildren() = copy(children = (children + 1).coerceAtMost(8))
    fun decrementChildren() = copy(children = (children - 1).coerceAtLeast(0))
}