package com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states

import com.hophey.jetgo.feature.searchFlights.domain.model.Flight

sealed class FoundFlightsUiState {
    object Idle : FoundFlightsUiState()
    object Loading : FoundFlightsUiState()
    data class Success(val flights: List<Flight>) : FoundFlightsUiState()
    data class Error(val message: String) : FoundFlightsUiState()
}