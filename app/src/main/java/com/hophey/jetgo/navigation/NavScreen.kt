package com.hophey.jetgo.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainScreen(
    navController: NavHostController,
){
    Scaffold(
        bottomBar = {
            FlightBottomNavBar(
                navigateToSearch = {
                    navController.navigate(Search) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToFavourite = {
                    navController.navigate(Favourites) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToProfile = {
                    navController.navigate(Profile) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
                    },
    ) { contentPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun FlightBottomNavBar(
    navigateToSearch: () -> Unit,
    navigateToFavourite: () -> Unit,
    navigateToProfile: () -> Unit
) {
    var selected by remember { mutableIntStateOf(0) }
    val items = listOf(
        Triple(Icons.Outlined.Search, "Поиск", navigateToSearch),
        Triple(Icons.Outlined.FavoriteBorder, "Избранное", navigateToFavourite),
//        Icons.Outlined.ConfirmationNumber to "Заказы",
        Triple(Icons.Outlined.Person, "Профиль", navigateToProfile)
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, (icon, label, action) ->
            NavigationBarItem(
                selected = selected == index,
                onClick = {
                    selected = index
                    action()
                },
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