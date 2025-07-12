package com.example.flourish.ui.components.CustomBottomAppBar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.flourish.ui.navigation.NavigationRoute

@Composable
fun BottomAppBarHandler(
    navController: NavHostController
) {
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route

    when{
        currentRoute == NavigationRoute.Homepage.route ||
        currentRoute == NavigationRoute.Sleep.route ||
        currentRoute == NavigationRoute.Mood.route ||
        currentRoute == NavigationRoute.Calendar.route ||
        currentRoute == NavigationRoute.Exercises.route ||
        currentRoute == NavigationRoute.Stats.route ||
        currentRoute == NavigationRoute.Profile.route -> {
            CustomBottomAppBar(navController = navController)
        }
    }
}