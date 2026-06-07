package com.hophey.jetgo.feature.searchFlights.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.hophey.jetgo.R
import com.hophey.jetgo.feature.searchFlights.domain.model.Flight
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.SearchFlightsSharedViewModel
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FlightSearchParams
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FoundFlightsUiState
import com.hophey.jetgo.utils.toDayAndMonth

@Composable
fun FoundFlightsScreenRoot(
    viewModel: SearchFlightsSharedViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchParams by viewModel.searchParams.collectAsStateWithLifecycle()

    Scaffold(
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
            onRetry = {
                searchParams?.let { viewModel.search(it) }
            },
            onPriceSort = viewModel::sortByPrice,
            onTimeSort = viewModel::sortByTime,
            onDefaultSort = viewModel::sortByDefault,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun FoundFlightsScreen(
    uiState: FoundFlightsUiState,
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
                        onPriceSort = onPriceSort,
                        onTimeSort = onTimeSort,
                        onDefaultSort = onDefaultSort
                    )
                    FlightsList(uiState.flights)
                }
            }
            is FoundFlightsUiState.Idle -> Unit
        }
    }
}

@Composable
fun SortRow(
    modifier: Modifier = Modifier,
    onPriceSort: () -> Unit,
    onTimeSort: () -> Unit,
    onDefaultSort: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        Button(onClick = { onDefaultSort() }) { Text("По порядку") }

        Button(modifier = Modifier.padding(horizontal = 8.dp), onClick = { onPriceSort() }) { Text("Цена") }

        Button( onClick = { onTimeSort() }) { Text("Время") }
    }
}

@Composable
fun FlightsList(foundFlights: List<Flight>) {
    LazyColumn {
        items(foundFlights) { item ->
            FlightCard(flight = item)
        }
    }
}

@Composable
fun FlightCard(flight: Flight) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        modifier = Modifier.alpha(0.4f),
                        text = flight.flightNum,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "от ${flight.price} ₽",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = flight.arrivalCountry,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                AsyncImage(
                    model = flight.airlineLogo,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = flight.departureCity.replace(Regex("[\\s\\-–—―‐‑‒⁃﹘﹣－]+"), "-\n"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        softWrap = true,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = flight.departureAirport,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = flight.departureTime, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${flight.timeTravel} " + stringResource(R.string.travel_time_text),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = flight.arrivalTime, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 1.dp
                        )
                        Icon(
                            imageVector = Icons.Outlined.Flight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 1.dp
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = flight.departureDate, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = flight.arrivalDate, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = flight.arrivalCity.replace(Regex("[\\s\\-–—―‐‑‒⁃﹘﹣－]+"), "-\n"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        softWrap = true,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = flight.arrivalAirport,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
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