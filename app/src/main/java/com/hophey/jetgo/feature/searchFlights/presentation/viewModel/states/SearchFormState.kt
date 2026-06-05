package com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states

import com.hophey.jetgo.feature.searchFlights.domain.model.Airport
import com.hophey.jetgo.feature.searchFlights.domain.model.CitySearchResult
import com.hophey.jetgo.feature.searchFlights.domain.model.PassengerCount
import kotlinx.datetime.LocalDate

enum class ActiveSheet { NONE, ORIGIN, DESTINATION, DATE, PASSENGERS }


data class FlightSearchParams(
    val departureCity: String,
    val arrivalCity: String,
    val departureDate: LocalDate,
    val passengers: PassengerCount
)
data class SearchFormState(
    val departureAirport: Airport? = null,
    val arrivalAirport: Airport? = null,
    val departureDate: String? = null,
    val passengers: PassengerCount = PassengerCount(),

    val activeSheet: ActiveSheet = ActiveSheet.NONE,

    val airportQuery: String = "",
    val airportSearchState: AirportSheetState = AirportSheetState.Idle,

    val canSearch: Boolean = false

) {
    val departureLabel: String get() = departureAirport?.let { "${it.cityName} (${it.code})" } ?: ""
    val arrivalLabel: String get() = arrivalAirport?.let { "${it.cityName} (${it.code})" } ?: ""
    val passengersLabel: String get() = buildString {
        append("${passengers.adults} взр.")
        if (passengers.children > 0) append(", ${passengers.children} дет.")
    }
}

sealed class AirportSheetState {
    data object Idle : AirportSheetState()
    data object Loading : AirportSheetState()
    data class Success(val result: CitySearchResult) : AirportSheetState()
    data class Error(val message: String) : AirportSheetState()
}