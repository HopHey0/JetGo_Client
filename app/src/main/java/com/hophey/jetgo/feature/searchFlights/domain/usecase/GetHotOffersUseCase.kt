package com.hophey.jetgo.feature.searchFlights.domain.usecase

import com.hophey.jetgo.feature.searchFlights.domain.model.HotOffer
import com.hophey.jetgo.feature.searchFlights.domain.model.toDomain
import com.hophey.jetgo.feature.searchFlights.domain.repository.FlightRepository

class GetHotOffersUseCase (
    private val flightRepository: FlightRepository
) {
    suspend fun invoke(): Result<List<HotOffer>> = runCatching {
        flightRepository.getHotOffers()
            .getOrThrow()
            .offers
            .map { it.toDomain() }
    }
}

class FakeGetHotOffersUseCase(){
    fun invoke(): Result<List<HotOffer>> = runCatching {
        listOf<HotOffer>(
            HotOffer(
                id = 1,
                price = 4500,
                priceWithDiscount = 3150,
                departureDate = "24 мая",
                departureTime = "09:00",
                departureAirport = "ДМД",
                departureCity = "Москва",
                arrivalTime = "11:30",
                arrivalAirport = "Пулково",
                arrivalCity = "Санкт-Петербург",
                arrivalCountry = "Россия",
                airlineName = "Аэрофлот",
                airlineLogo = "aeroflot_logo.png",
                discountRate = 30,
                timeTravel = "2ч 30мин"
            ),
            HotOffer(
                id = 2,
                price = 12500,
                priceWithDiscount = 8750,
                departureDate = "5 июня",
                departureTime = "07:15",
                departureAirport = "Внуково",
                departureCity = "Москва",
                arrivalTime = "10:45",
                arrivalAirport = "Адлер",
                arrivalCity = "Сочи",
                arrivalCountry = "Россия",
                airlineName = "S7 Airlines",
                airlineLogo = "s7_logo.png",
                discountRate = 30,
                timeTravel = "3ч 30мин"
            ),
        HotOffer(
            id = 3,
            price = 8900,
            priceWithDiscount = 6230,
            departureDate = "12 июня",
            departureTime = "14:20",
            departureAirport = "Пулково",
            departureCity = "Санкт-Петербург",
            arrivalTime = "16:15",
            arrivalAirport = "Храброво",
            arrivalCity = "Калининград",
            arrivalCountry = "Россия",
            airlineName = "Победа",
            airlineLogo = "pobeda_logo.png",
            discountRate = 30,
            timeTravel = "1ч 55мин"
        ),
        HotOffer(
            id = 4,
            price = 9800,
            priceWithDiscount = 6860,
            departureDate = "18 июня",
            departureTime = "22:40",
            departureAirport = "Шереметьево",
            departureCity = "Москва",
            arrivalTime = "03:10",
            arrivalAirport = "Кольцово",
            arrivalCity = "Екатеринбург",
            arrivalCountry = "Россия",
            airlineName = "Уральские авиалинии",
            airlineLogo = "ural_logo.png",
            discountRate = 30,
            timeTravel = "2ч 30мин"
        ),
        HotOffer(
            id = 5,
            price = 18700,
            priceWithDiscount = 13090,
            departureDate = "1 июля",
            departureTime = "19:05",
            departureAirport = "Толмачёво",
            departureCity = "Новосибирск",
            arrivalTime = "02:45",
            arrivalAirport = "Кневичи",
            arrivalCity = "Владивосток",
            arrivalCountry = "Россия",
            airlineName = "Россия",
            airlineLogo = "rossiya_logo.png",
            discountRate = 30,
            timeTravel = "4ч 40мин"
        )
        )
    }
}