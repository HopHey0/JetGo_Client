package com.hophey.jetgo.feature.searchFlights.domain.usecase

import com.hophey.jetgo.feature.searchFlights.domain.model.HotOffer
import com.hophey.jetgo.feature.searchFlights.domain.model.toDomain
import com.hophey.jetgo.feature.searchFlights.domain.repository.FlightRepository

class GetHotOffersUseCase (
    private val flightRepository: FlightRepository
) {
    suspend operator fun invoke(): Result<List<HotOffer>> = runCatching {
        flightRepository.getHotOffers()
            .getOrThrow()
            .offers
            .map { it.toDomain() }
    }
}