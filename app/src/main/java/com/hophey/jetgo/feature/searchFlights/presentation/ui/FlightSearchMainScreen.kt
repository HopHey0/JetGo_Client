package com.hophey.jetgo.feature.searchFlights.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.FlightLand
import androidx.compose.material.icons.outlined.FlightTakeoff
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.hophey.jetgo.R
import com.hophey.jetgo.feature.searchFlights.domain.model.HotOffer
import com.hophey.jetgo.feature.searchFlights.presentation.ui.bottomSheets.AirportPickerSheet
import com.hophey.jetgo.feature.searchFlights.presentation.ui.bottomSheets.DatePickerSheet
import com.hophey.jetgo.feature.searchFlights.presentation.ui.bottomSheets.PassengerPickerSheet
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.FlightSearchMainScreenViewModel
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.ActiveSheet
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.FlightSearchParams
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.HotOffersUiState
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.states.SearchFormState
import com.hophey.jetgo.theme.JetGoTheme
import com.hophey.jetgo.utils.toDayAndMonth
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.koinViewModel

data class RecentSearch(
    val origin: String,
    val destination: String,
    val date: String,
    val passengers: Int
)

val recentSearches = listOf(
    RecentSearch("Москва", "Белград", "16 мая", 2),
    RecentSearch("Белград", "Москва", "20 мая", 1),
    RecentSearch("Москва", "Стамбул", "1 июня", 3)
)

@Composable
fun FlightSearchRoot(
    onNavigateToResults: (FlightSearchParams) -> Unit = {},
    viewModel: FlightSearchMainScreenViewModel = koinViewModel()
) {
    val hotOffersState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchForm by viewModel.searchForm.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { FlightBottomNavBar() },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        FlightSearchScreen(
            hotOffersUiState = hotOffersState,
            searchForm = searchForm,
            onOriginClick = viewModel::openOriginSheet,
            onDestinationClick = viewModel::openDestinationSheet,
            onDateClick = viewModel::openDateSheet,
            onPassengersClick = viewModel::openPassengersSheet,
            onSearchClick = { viewModel.onSearchClicked(onNavigateToResults) },
            onRetry = viewModel::getHotOffers,
            contentPadding = innerPadding,
            recentSearches = recentSearches
        )
    }


    when (searchForm.activeSheet) {
        ActiveSheet.ORIGIN -> AirportPickerSheet(
            title = "Откуда",
            query = searchForm.airportQuery,
            searchState = searchForm.airportSearchState,
            onQueryChange = viewModel::onAirportQueryChanged,
            onAirportSelected = viewModel::selectAirport,
            onDismiss = viewModel::closeSheet
        )

        ActiveSheet.DESTINATION -> AirportPickerSheet(
            title = "Куда",
            query = searchForm.airportQuery,
            searchState = searchForm.airportSearchState,
            onQueryChange = viewModel::onAirportQueryChanged,
            onAirportSelected = viewModel::selectAirport,
            onDismiss = viewModel::closeSheet
        )

        ActiveSheet.DATE -> DatePickerSheet(
            selectedDate = searchForm.departureDate?.let { LocalDate.parse(it) },
            onDateSelected = viewModel::selectDate,
            onDismiss = viewModel::closeSheet
        )

        ActiveSheet.PASSENGERS -> PassengerPickerSheet(
            passengers = searchForm.passengers,
            onIncrementAdults = viewModel::incrementAdults,
            onDecrementAdults = viewModel::decrementAdults,
            onIncrementChildren = viewModel::incrementChildren,
            onDecrementChildren = viewModel::decrementChildren,
            onConfirm = viewModel::confirmPassengers,
            onDismiss = viewModel::closeSheet
        )

        ActiveSheet.NONE -> Unit
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlightSearchScreen(
    recentSearches: List<RecentSearch>,
    hotOffersUiState: HotOffersUiState,
    searchForm: SearchFormState,
    onOriginClick: () -> Unit,
    onDestinationClick: () -> Unit,
    onDateClick: () -> Unit,
    onPassengersClick: () -> Unit,
    onSearchClick: () -> Unit,
    onRetry: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        item {
            SearchSection(
                searchForm = searchForm,
                onOriginClick = onOriginClick,
                onDestinationClick = onDestinationClick,
                onDateClick = onDateClick,
                onPassengersClick = onPassengersClick,
                onSearchClick = onSearchClick
            )
        }

        if (recentSearches.isNotEmpty()) {
            item {
                SectionHeader(icon = Icons.Outlined.Schedule, title = stringResource(R.string.section_header_recent_searches))
            }
            item {
                RecentSearchList(searches = recentSearches.take(3))
            }
        }

        item {
            SectionHeader(icon = Icons.Outlined.LocalFireDepartment, title = stringResource(R.string.section_header_hot_offers))
        }

        when (hotOffersUiState){
            is HotOffersUiState.Success -> item {
                HotOffersPager(offers = hotOffersUiState.hotOffers)
                Spacer(modifier = Modifier.height(16.dp))
            }
            else -> item {
                HotOffersErrorOrLoad(
                    hotOffersUiState,
                    onRetry
                )
            }
        }
    }
}

@Composable
private fun SearchSection(
    searchForm: SearchFormState,
    onOriginClick: () -> Unit,
    onDestinationClick: () -> Unit,
    onDateClick: () -> Unit,
    onPassengersClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 32.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                SearchField(
                    label = stringResource(R.string.departure_search_field_label),
                    value = searchForm.departureLabel,
                    icon = Icons.Outlined.FlightTakeoff,
                    placeholder = stringResource(R.string.search_field_placeholder),
                    onClick = onOriginClick
                )

                SearchField(
                    label = stringResource(R.string.arrival_search_field_label),
                    value = searchForm.arrivalLabel,
                    icon = Icons.Outlined.FlightLand,
                    placeholder = stringResource(R.string.search_field_placeholder),
                    onClick = onDestinationClick
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SmallField(
                        modifier = Modifier.weight(1f),
                        label = stringResource(R.string.flight_date_label),
                        value = searchForm.departureDate?.toDayAndMonth()
                            ?: "",
                        icon = Icons.Outlined.CalendarMonth,
                        placeholder = stringResource(R.string.flight_date_placeholder),
                        onClick = onDateClick
                    )
                    SmallField(
                        modifier = Modifier.weight(1f),
                        label = stringResource(R.string.passengers_field_label),
                        value = searchForm.passengersLabel,
                        icon = Icons.Outlined.Person,
                        placeholder = stringResource(R.string.passengers_field_placeholder),
                        onClick = onPassengersClick
                    )
                }

                Button(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.search_button_text),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentSearchList(searches: List<RecentSearch>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            searches.forEachIndexed { index, search ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${search.origin} → ${search.destination}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${search.date}  •  ${search.passengers} " + stringResource(R.string.recent_searches_passengers_amount),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
                if (index < searches.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HotOffersErrorOrLoad(
    uiState: HotOffersUiState,
    onRetry: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues = PaddingValues(16.dp))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(192.dp)
                .padding(top = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (uiState) {
                    is HotOffersUiState.Error -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = uiState.message,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                        ){
                            IconButton(
                                onClick = { onRetry() }
                            ){
                                Icon(
                                    imageVector = Icons.Outlined.Replay,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(24.dp),
                                )
                            }
                        }
                    }

                    is HotOffersUiState.Loading ->
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp)
                        )

                    else -> {}
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HotOffersPager(
    offers: List<HotOffer>
) {
    val pagerState = rememberPagerState { offers.size }

    Column {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            HotOfferCard(offer = offers[page])
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(offers.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isSelected) 8.dp else 6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            }
        }
    }
}

@Composable
private fun HotOfferCard(offer: HotOffer) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${offer.priceWithDiscount} ₽",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${offer.price} ₽",
                                fontSize = 20.sp,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.alpha(0.4f)
                            )
                        }
                        Spacer(modifier = Modifier.padding(vertical = 4.dp))
                        Text(
                            text = offer.arrivalCountry,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Box {
                        AsyncImage(
                            model = offer.airlineLogo,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = offer.departureCity
                                .replace(" ", "\n")
                                .replace("–", "\n"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            softWrap = true,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = offer.departureAirport,
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = offer.departureTime,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = offer.arrivalTime,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = offer.departureDate,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "${offer.timeTravel} " + stringResource(R.string.travel_time_text),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = offer.arrivalDate,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = offer.arrivalCity.replace(" ", "\n").replace("–", "\n"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            softWrap = true,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = offer.arrivalAirport,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 16.dp, y = 0.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.hot_offer_percent_text) + " ${offer.discountRate} %",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Composable
private fun SearchField(
    label: String,
    value: String,
    icon: ImageVector,
    placeholder: String = "",
    onClick: () -> Unit
) {
    val isEmpty = value.isBlank()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (isEmpty) placeholder else value,
                    fontSize = 15.sp,
                    fontWeight = if (isEmpty) FontWeight.Normal else FontWeight.Medium,
                    color = if (isEmpty) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun SmallField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    placeholder: String = "",
    onClick: () -> Unit
) {
    val isEmpty = value.isBlank()
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (isEmpty) placeholder else value,
                    fontSize = 13.sp,
                    fontWeight = if (isEmpty) FontWeight.Normal else FontWeight.Medium,
                    color = if (isEmpty) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FlightBottomNavBar() {
    var selected by remember { mutableIntStateOf(0) }
    val items = listOf(
        Icons.Outlined.Search to "Поиск",
        Icons.Outlined.FavoriteBorder to "Избранное",
        Icons.Outlined.ConfirmationNumber to "Заказы",
        Icons.Outlined.Person to "Профиль"
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, (icon, label) ->
            NavigationBarItem(
                selected = selected == index,
                onClick = { selected = index },
                icon = { Icon(imageVector = icon, contentDescription = label) },
                label = { Text(text = label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Preview(
    name = "Flight Search Screen",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=390dp,height=844dp,dpi=420"
)
@Composable
fun FlightSearchScreenPreview() {
    JetGoTheme {
        FlightSearchScreen(
            recentSearches = recentSearches,
            onRetry = { },
            hotOffersUiState = HotOffersUiState.Error("Error"),
            searchForm = SearchFormState(),
            onOriginClick = { },
            onDestinationClick = { },
            onDateClick = { },
            onPassengersClick = { },
            onSearchClick = { },
            contentPadding = PaddingValues(0.dp)
        )
    }
}