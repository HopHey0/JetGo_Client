package com.hophey.jetgo.feature.searchFlights.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hophey.jetgo.feature.searchFlights.domain.usecase.SearchFlightsUseCase
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FlightSearchParams
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FoundFlightsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchFlightsSharedViewModel(
    private val searchFlightsUseCase: SearchFlightsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FoundFlightsUiState>(FoundFlightsUiState.Idle)
    val uiState: StateFlow<FoundFlightsUiState> = _uiState.asStateFlow()

    private val _searchParams = MutableStateFlow<FlightSearchParams?>(null)
    val searchParams: StateFlow<FlightSearchParams?> = _searchParams.asStateFlow()

    fun search(params: FlightSearchParams) {
        _searchParams.value = params
        viewModelScope.launch {
            _uiState.value = FoundFlightsUiState.Loading
            searchFlightsUseCase(params)
                .onSuccess { _uiState.value = FoundFlightsUiState.Success(it) }
                .onFailure { _uiState.value = FoundFlightsUiState.Error(it.message ?: "Something went wrong") }
        }
    }

    fun sortByPrice() {
        val current = _uiState.value as? FoundFlightsUiState.Success ?: return
        _uiState.value = current.copy(flights = current.flights.sortedBy { it.price })
    }

    fun sortByTime() {
        val current = _uiState.value as? FoundFlightsUiState.Success ?: return
        _uiState.value = current.copy(flights = current.flights.sortedBy { it.timeTravel })
    }

    fun sortByDefault() {
        val current = _uiState.value as? FoundFlightsUiState.Success ?: return
        _uiState.value = current.copy(flights = current.flights.sortedBy { it.departureTime })
    }
}