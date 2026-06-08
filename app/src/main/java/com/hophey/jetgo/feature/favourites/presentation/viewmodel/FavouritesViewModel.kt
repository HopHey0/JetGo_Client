package com.hophey.jetgo.feature.favourites.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hophey.jetgo.feature.favourites.domain.usecase.GetAccessTokenUseCase
import com.hophey.jetgo.feature.favourites.domain.usecase.GetFavouritesUseCase
import com.hophey.jetgo.feature.favourites.domain.usecase.ToggleFavouriteUseCase
import com.hophey.jetgo.feature.searchFlights.domain.model.Flight
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavouritesViewModel(
    getFavouritesUseCase: GetFavouritesUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    val favourites: StateFlow<List<Flight>> = getFavouritesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun remove(flight: Flight) {
        viewModelScope.launch {
            val loggedIn = getAccessTokenUseCase() != null
            toggleFavouriteUseCase(flight, loggedIn)
        }
    }
}