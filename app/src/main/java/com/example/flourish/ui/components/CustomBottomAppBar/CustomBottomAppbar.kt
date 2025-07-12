package com.example.flourish.ui.components.CustomBottomAppBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.flourish.R
import com.example.flourish.ui.navigation.NavigationRoute

@Composable
fun CustomBottomAppBar(
    navController: NavHostController
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate(NavigationRoute.Homepage.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.bottom_app_bar_plant),
                    contentDescription = "plant",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            IconButton(onClick = { navController.navigate(NavigationRoute.Sleep.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.bottom_app_bar_emoji),
                    contentDescription = "emoji",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            IconButton(onClick = { navController.navigate(NavigationRoute.Calendar.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.bottom_app_bar_calendar),
                    contentDescription = "calendar",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            IconButton(onClick = { navController.navigate(NavigationRoute.Exercises.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.bottom_app_bar_mindfulness),
                    contentDescription = "mindfulness",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            IconButton(onClick = { navController.navigate(NavigationRoute.Stats.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.bottom_app_bar_stats),
                    contentDescription = "stats",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            IconButton(onClick = { navController.navigate(NavigationRoute.Profile.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.bottom_app_bar_profile),
                    contentDescription = "profile",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}