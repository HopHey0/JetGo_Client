package com.hophey.jetgo.feature.searchFlights.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hophey.jetgo.feature.searchFlights.domain.model.HotOffer
import com.hophey.jetgo.feature.searchFlights.domain.usecase.GetHotOffersUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HotOffersUiState {
    object Loading : HotOffersUiState()
    data class Success(val hotOffers: List<HotOffer>) : HotOffersUiState()
    data class Error(val message: String) : HotOffersUiState()
}

class FlightSearchMainScreenViewModel(
    private val getHotOffersUseCase: GetHotOffersUseCase
) : ViewModel() {
    val _uiState = MutableStateFlow<HotOffersUiState>(HotOffersUiState.Loading)
    var uiState = _uiState.asStateFlow()

    var getHotOffersJob: Job? = null

    init {
        getHotOffers()
    }

    fun getHotOffers() {
        getHotOffersJob?.cancel()
        getHotOffersJob = viewModelScope.launch {

            val result = getHotOffersUseCase.invoke()

            result
                .onSuccess { list ->
                    _uiState.value = HotOffersUiState.Success(list)
                }
                .onFailure { error ->
                    _uiState.value = HotOffersUiState.Error(error.message ?: "Error while loading")
                }
        }
    }
}