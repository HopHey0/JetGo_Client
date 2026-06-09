package com.hophey.jetgo.feature.searchFlights.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hophey.jetgo.core.shared.ui.FlightCard
import com.hophey.jetgo.feature.searchFlights.domain.model.Flight
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.SearchFlightsSharedViewModel
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.SortButtonsState
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FlightSearchParams
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FoundFlightsUiState
import com.hophey.jetgo.utils.toDayAndMonth

@Composable
fun FoundFlightsScreenRoot(
    viewModel: SearchFlightsSharedViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchParams by viewModel.searchParams.collectAsStateWithLifecycle()
    val buttonsState by viewModel.buttonsState.collectAsStateWithLifecycle()
    val favouritesId by viewModel.favouriteIds.collectAsStateWithLifecycle()
    val requireAuth by viewModel.requireAuth.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(requireAuth) {
        if (requireAuth) {
            Toast.makeText(context, "Войдите в аккаунт, чтобы добавить в избранное", Toast.LENGTH_SHORT).show()
            viewModel.consumeRequireAuth()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            FoundFlightsTopBar(
                searchParams = searchParams,
                onBack = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        FoundFlightsScreen(
            uiState = uiState,
            buttonsState = buttonsState,
            onRetry = {
                searchParams?.let { viewModel.search(it) }
            },
            onPriceSort = viewModel::sortByPrice,
            onTimeSort = viewModel::sortByTime,
            onDefaultSort = viewModel::sortByDefault,
            modifier = Modifier.padding(innerPadding),
            favouritesId = favouritesId,
            onFavouriteClick = viewModel::toggleFavourite
        )
    }
}

@Composable
fun FoundFlightsScreen(
    uiState: FoundFlightsUiState,
    buttonsState: SortButtonsState,
    favouritesId: List<Long>,
    onFavouriteClick: (Flight) -> Unit,
    onRetry: () -> Unit,
    onPriceSort: () -> Unit,
    onTimeSort: () -> Unit,
    onDefaultSort: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (uiState) {
            is FoundFlightsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is FoundFlightsUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.message,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onRetry() }
                    ) {
                        Text(
                            text = "Повторить"
                        )
                    }
                }
            }
            is FoundFlightsUiState.Success -> {
                if (uiState.flights.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ничего не нашлось :(",
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    SortRow(
                        buttonsState = buttonsState,
                        onPriceSort = onPriceSort,
                        onTimeSort = onTimeSort,
                        onDefaultSort = onDefaultSort
                    )
                    FlightsList(
                        uiState.flights,
                        favouriteIds = favouritesId,
                        onFavouriteClick = onFavouriteClick
                    )
                }
            }
            is FoundFlightsUiState.Idle -> Unit
        }
    }
}

@Composable
fun SortRow(
    buttonsState: SortButtonsState,
    onPriceSort: () -> Unit,
    onTimeSort: () -> Unit,
    onDefaultSort: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        Button(
            onClick = { onDefaultSort() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (buttonsState.isDefaultEnabled)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.secondary
            )
        ) { Text("По порядку") }

        Button(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = { onPriceSort() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (buttonsState.isPriceEnabled)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.secondary
            )
        ) { Text("Цена") }

        Button(
            onClick = { onTimeSort() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (buttonsState.isTimeEnabled)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.secondary
            )
        ) { Text("Время") }
    }
}

@Composable
fun FlightsList(
    foundFlights: List<Flight>,
    favouriteIds: List<Long>,
    onFavouriteClick: (Flight) -> Unit
) {
    LazyColumn {
        items(foundFlights) { item ->
            FlightCard(
                flight = item,
                isFavourite = item.id in favouriteIds,
                onFavouriteClick = { onFavouriteClick(item) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundFlightsTopBar(
    searchParams: FlightSearchParams?,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = if (searchParams != null)
                        "${searchParams.departureCity} → ${searchParams.arrivalCity}"
                    else "",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = if (searchParams != null)
                        "${searchParams.departureDate.toString().toDayAndMonth()} · ${searchParams.passengers} пас."
                    else "",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}