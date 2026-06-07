package com.hophey.jetgo.feature.searchFlights.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hophey.jetgo.feature.searchFlights.domain.model.Airport
import com.hophey.jetgo.feature.searchFlights.domain.usecase.GetHotOffersUseCase
import com.hophey.jetgo.feature.searchFlights.domain.usecase.SearchAirportsUseCase
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.ActiveSheet
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.AirportSheetState
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FlightSearchParams
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.HotOffersUiState
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.SearchFormState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate


@OptIn(FlowPreview::class)
class FlightSearchMainScreenViewModel(
    private val getHotOffersUseCase: GetHotOffersUseCase,
    private val searchAirportsUseCase: SearchAirportsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HotOffersUiState>(HotOffersUiState.Loading)
    val uiState: StateFlow<HotOffersUiState> = _uiState.asStateFlow()

    private val _searchForm = MutableStateFlow(SearchFormState())
    val searchForm: StateFlow<SearchFormState> = _searchForm.asStateFlow()


    init {
        getHotOffers()
        observeAirportQuery()
    }

    fun getHotOffers() {
        viewModelScope.launch {
            _uiState.value = HotOffersUiState.Loading
            getHotOffersUseCase()
                .onSuccess { _uiState.value = HotOffersUiState.Success(it) }
                .onFailure { _uiState.value = HotOffersUiState.Error(it.message ?: "Something went wrong") }
        }
    }

    fun openOriginSheet() = _searchForm.update {
        it.copy(activeSheet = ActiveSheet.ORIGIN, airportQuery = "", airportSearchState = AirportSheetState.Idle)
    }

    fun openDestinationSheet() = _searchForm.update {
        it.copy(activeSheet = ActiveSheet.DESTINATION, airportQuery = "", airportSearchState = AirportSheetState.Idle)
    }

    fun openDateSheet() = _searchForm.update { it.copy(activeSheet = ActiveSheet.DATE) }

    fun openPassengersSheet() = _searchForm.update { it.copy(activeSheet = ActiveSheet.PASSENGERS) }

    fun closeSheet() = _searchForm.update { it.copy(activeSheet = ActiveSheet.NONE) }

    fun onAirportQueryChanged(query: String) = _searchForm.update {
        it.copy(airportQuery = query, airportSearchState = AirportSheetState.Idle)
    }

    private fun observeAirportQuery() {
        _searchForm
            .map { it.airportQuery }
            .distinctUntilChanged()
            .debounce(400L)
            .onEach { query -> if (query.isNotBlank()) performAirportSearch(query) }
            .launchIn(viewModelScope)
    }

    private fun performAirportSearch(query: String) {
        viewModelScope.launch {
            _searchForm.update { it.copy(airportSearchState = AirportSheetState.Loading) }
            val result = searchAirportsUseCase(query)
            _searchForm.update {
                it.copy(
                    airportSearchState = when {
                        result == null -> AirportSheetState.Idle
                        result.isSuccess -> AirportSheetState.Success(result.getOrThrow())
                        else -> AirportSheetState.Error(result.exceptionOrNull()?.message ?: "Ошибка")
                    }
                )
            }
        }
    }

    fun selectAirport(airport: Airport) {
        _searchForm.update { form ->
            when (form.activeSheet) {
                ActiveSheet.ORIGIN -> form.copy(
                    departureAirport = airport,
                    activeSheet = ActiveSheet.NONE,
                    airportQuery = ""
                )
                ActiveSheet.DESTINATION -> form.copy(
                    arrivalAirport = airport,
                    activeSheet = ActiveSheet.NONE,
                    airportQuery = ""
                )
                else -> form
            }.recalculateCanSearch()
        }
    }

    fun selectDate(date: LocalDate) {
        _searchForm.update {
            it.copy(departureDate = date.toString(), activeSheet = ActiveSheet.NONE).recalculateCanSearch()
        }
    }

    fun incrementAdults() = _searchForm.update { it.copy(passengers = it.passengers.incrementAdults()) }
    fun decrementAdults() = _searchForm.update { it.copy(passengers = it.passengers.decrementAdults()) }
    fun incrementChildren() = _searchForm.update { it.copy(passengers = it.passengers.incrementChildren()) }
    fun decrementChildren() = _searchForm.update { it.copy(passengers = it.passengers.decrementChildren()) }

    fun confirmPassengers() = _searchForm.update {
        it.copy(activeSheet = ActiveSheet.NONE)
    }


    fun onSearchClicked(onNavigate: (FlightSearchParams) -> Unit) {
        val form = _searchForm.value
        val origin = form.departureAirport ?: return
        val destination = form.arrivalAirport ?: return
        val date = form.departureDate ?: return
        val params = FlightSearchParams(
            departureCity = origin.code,
            arrivalCity = destination.code,
            departureDate = LocalDate.parse(date),
            passengers = form.passengers.total
        )
        onNavigate(params)
    }

    private fun SearchFormState.recalculateCanSearch(): SearchFormState =
        copy(canSearch = departureAirport != null && arrivalAirport != null && departureDate != null)
}
