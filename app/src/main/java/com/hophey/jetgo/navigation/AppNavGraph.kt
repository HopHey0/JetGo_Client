package com.hophey.jetgo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hophey.jetgo.feature.searchFlights.presentation.ui.FlightSearchRoot
import com.hophey.jetgo.feature.searchFlights.presentation.ui.FoundFlightsScreenRoot
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.SearchFlightsSharedViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = SearchGraph
    ) {
        navigation<SearchGraph>(startDestination = Search) {

            composable<Search> {
                val sharedViewModel: SearchFlightsSharedViewModel =
                    koinViewModel(viewModelStoreOwner = navController.getBackStackEntry<SearchGraph>())

                FlightSearchRoot(
                    onNavigateToResults = { params ->
                        sharedViewModel.search(params)
                        navController.navigate(SearchResult) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<SearchResult> {
                val sharedViewModel: SearchFlightsSharedViewModel =
                    koinViewModel(viewModelStoreOwner = navController.getBackStackEntry<SearchGraph>())

                FoundFlightsScreenRoot(
                    viewModel = sharedViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}