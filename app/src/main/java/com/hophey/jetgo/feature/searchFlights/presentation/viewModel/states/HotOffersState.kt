package com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states

import com.hophey.jetgo.feature.searchFlights.domain.model.HotOffer
import com.hophey.jetgo.feature.searchFlights.domain.model.PassengerCount
import kotlinx.datetime.LocalDate

sealed class HotOffersUiState {
    object Loading : HotOffersUiState()
    data class Success(val hotOffers: List<HotOffer>) : HotOffersUiState()
    data class Error(val message: String) : HotOffersUiState()
}
