package com.hophey.jetgo.feature.searchFlights.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.hophey.jetgo.theme.JetGoTheme
import com.hophey.jetgo.R

data class RecentSearch(
    val origin: String,
    val destination: String,
    val date: String,
    val passengers: Int
)

data class HotOffer(
    val country: String,
    val price: Double,
    val originCity: String,
    val originAirport: String,
    val destCity: String,
    val destAirport: String,
    val airlineLogoUrl: String,
    val airlineCode: String,
    val offerPercent: Double,
    val departureDate: String,
    val arrivalDate: String
)

@Composable
fun FlightSearchRoot() {
    val recentSearches = listOf(
        RecentSearch("Москва", "Белград", "16 мая", 2),
        RecentSearch("Белград", "Москва", "20 мая", 1),
        RecentSearch("Москва", "Стамбул", "1 июня", 3)
    )
    val hotOffers = listOf(
        HotOffer("Турция", 56_400.0, "Москва", "SVO", "Стамбул", "IST", "https://logo-teka.com/wp-content/uploads/2025/08/aeroflot-logo-eng.png", "TK", 15.5, "08:30", "12:45"),
        HotOffer("Сербия", 9_800.0, "Москва", "SVO", "Белград", "BEG", "https://logo-teka.com/wp-content/uploads/2025/10/lufthansa-sign-logo.png", "JU", 12.0, "09:15", "11:30"),
        HotOffer("ОАЭ", 18_900.0, "Москва", "DME", "Дубай", "DXB", "https://logo-teka.com/wp-content/uploads/2025/10/airfrance-logo.png", "EK", 20.0, "10:00", "01:15"),
        HotOffer("Армения", 7_200.0, "Москва", "SVO", "Ереван", "EVN", "https://logo-teka.com/wp-content/uploads/2025/11/emirates-logo.png", "G9", 10.0, "07:45", "10:20")
    )

    Scaffold(
        bottomBar = { FlightBottomNavBar() },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        FlightSearchScreen(
            recentSearches = recentSearches,
            hotOffers = hotOffers,
            contentPadding = innerPadding
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlightSearchScreen(
    recentSearches: List<RecentSearch>,
    hotOffers: List<HotOffer>,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var origin by remember { mutableStateOf("Москва (SVO)") }
    var destination by remember { mutableStateOf("") }
    var dateDepart by remember { mutableStateOf("") }
    var passengers by remember { mutableStateOf("") }

    val pagerState = rememberPagerState { hotOffers.size }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        item {
            SearchSection(
                origin = origin,
                destination = destination,
                dateDepart = dateDepart,
                passengers = passengers,
                onSearch = {}
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
        item {
            HotOffersPager(offers = hotOffers, pagerState = pagerState)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SearchSection(
    origin: String,
    destination: String,
    dateDepart: String,
    passengers: String,
    onSearch: () -> Unit
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
                    value = origin,
                    icon = Icons.Outlined.FlightTakeoff,
                    placeholder = stringResource(R.string.search_field_placeholder),
                    onClick = {}
                )

                SearchField(
                    label = stringResource(R.string.arrival_search_field_label),
                    value = destination,
                    icon = Icons.Outlined.FlightLand,
                    placeholder = stringResource(R.string.search_field_placeholder),
                    onClick = {}
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SmallField(
                        modifier = Modifier.weight(1f),
                        label = stringResource(R.string.flight_date_label),
                        value = dateDepart,
                        icon = Icons.Outlined.CalendarMonth,
                        placeholder = stringResource(R.string.flight_date_placeholder),
                        onClick = {}
                    )
                    SmallField(
                        modifier = Modifier.weight(1f),
                        label = stringResource(R.string.passengers_field_label),
                        value = passengers,
                        icon = Icons.Outlined.Person,
                        placeholder = stringResource(R.string.passengers_field_placeholder),
                        onClick = {}
                    )
                }

                Button(
                    onClick = onSearch,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HotOffersPager(
    offers: List<HotOffer>,
    pagerState: PagerState
) {
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
                                text = "${(offer.price * (1 - (offer.offerPercent / 100))).toInt()} ₽",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${offer.price.toInt()} ₽",
                                fontSize = 20.sp,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.alpha(0.4f)
                            )
                        }
                        Spacer(modifier = Modifier.padding(vertical = 4.dp))
                        Text(
                            text = offer.country,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Box {
                        AsyncImage(
                            model = offer.airlineLogoUrl,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.height(32.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "25 мая",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = offer.originCity,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = offer.originAirport,
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
                                text = offer.departureDate,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = offer.arrivalDate,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
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
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                                color = MaterialTheme.colorScheme.outlineVariant,
                                thickness = 1.dp
                            )
                        }
                        Text(
                            text = "4,25 ч. в пути",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = offer.destCity,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = offer.destAirport,
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
                text = stringResource(R.string.hot_offer_percent_text) + " ${offer.offerPercent.toInt()} %",
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
        FlightSearchRoot()
    }
}