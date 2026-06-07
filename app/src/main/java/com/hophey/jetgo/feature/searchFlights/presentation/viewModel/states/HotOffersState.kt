package com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states

import com.hophey.jetgo.feature.searchFlights.domain.model.Flight

sealed class HotOffersUiState {
    object Loading : HotOffersUiState()
    data class Success(val hotOffers: List<Flight>) : HotOffersUiState()
    data class Error(val message: String) : HotOffersUiState()
}
