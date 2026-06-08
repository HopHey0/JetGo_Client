package com.hophey.jetgo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hophey.jetgo.feature.auth.presentation.ui.ProfileScreen
import com.hophey.jetgo.feature.auth.presentation.viewmodel.ProfileViewModel
import com.hophey.jetgo.feature.favourites.presentation.ui.FavouritesScreen
import com.hophey.jetgo.feature.favourites.presentation.viewmodel.FavouritesViewModel
import com.hophey.jetgo.feature.searchFlights.presentation.ui.FlightSearchRoot
import com.hophey.jetgo.feature.searchFlights.presentation.ui.FoundFlightsScreenRoot
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.SearchFlightsSharedViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
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
                    modifier = modifier,
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
                    modifier = modifier,
                    viewModel = sharedViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable<Profile> {
            val profileViewModel: ProfileViewModel = koinViewModel()

            ProfileScreen(
                modifier = modifier,
                viewModel = profileViewModel
            )
        }

        composable<Favourites> {
            val favouritesViewModel: FavouritesViewModel = koinViewModel()

            FavouritesScreen(
                modifier = modifier,
                viewModel = favouritesViewModel
            )
        }
    }
}