package com.hophey.jetgo.feature.searchFlights.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hophey.jetgo.feature.favourites.domain.usecase.GetAccessTokenUseCase
import com.hophey.jetgo.feature.favourites.domain.usecase.GetFavouritesUseCase
import com.hophey.jetgo.feature.favourites.domain.usecase.ToggleFavouriteUseCase
import com.hophey.jetgo.feature.searchFlights.domain.model.Flight
import com.hophey.jetgo.feature.searchFlights.domain.usecase.SearchFlightsUseCase
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FlightSearchParams
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FoundFlightsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SortButtonsState(
    val isDefaultEnabled: Boolean = true,
    val isPriceEnabled: Boolean = false,
    val isTimeEnabled: Boolean = false
)

class SearchFlightsSharedViewModel(
    private val searchFlightsUseCase: SearchFlightsUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    getFavouritesUseCase: GetFavouritesUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FoundFlightsUiState>(FoundFlightsUiState.Idle)
    val uiState: StateFlow<FoundFlightsUiState> = _uiState.asStateFlow()

    private val _searchParams = MutableStateFlow<FlightSearchParams?>(null)
    val searchParams: StateFlow<FlightSearchParams?> = _searchParams.asStateFlow()

    private val _buttonsState = MutableStateFlow<SortButtonsState>(SortButtonsState())
    val buttonsState = _buttonsState.asStateFlow()

    val favouriteIds: StateFlow<List<Long>> = getFavouritesUseCase.ids()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _requireAuth = MutableStateFlow(false)
    val requireAuth: StateFlow<Boolean> = _requireAuth.asStateFlow()

    fun consumeRequireAuth() {
        _requireAuth.value = false
    }

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
        _buttonsState.value = _buttonsState.value.copy(
            isDefaultEnabled = false,
            isPriceEnabled = true,
            isTimeEnabled = false
        )
    }

    fun sortByTime() {
        val current = _uiState.value as? FoundFlightsUiState.Success ?: return
        _uiState.value = current.copy(flights = current.flights.sortedBy { it.timeTravel })
        _buttonsState.value = _buttonsState.value.copy(
            isDefaultEnabled = false,
            isPriceEnabled = false,
            isTimeEnabled = true
        )
    }

    fun sortByDefault() {
        val current = _uiState.value as? FoundFlightsUiState.Success ?: return
        _uiState.value = current.copy(flights = current.flights.sortedBy { it.departureTime })
        _buttonsState.value = _buttonsState.value.copy(
            isDefaultEnabled = true,
            isPriceEnabled = false,
            isTimeEnabled = false
        )
    }

    fun toggleFavourite(flight: Flight) {
        viewModelScope.launch {
            val loggedIn = getAccessTokenUseCase() != null
            if (!loggedIn) {
                _requireAuth.value = true
                return@launch
            }
            toggleFavouriteUseCase(flight, isLoggedIn = true)
        }
    }
}