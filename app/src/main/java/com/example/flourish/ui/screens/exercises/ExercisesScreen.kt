package com.example.flourish.ui.screens.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.flourish.R
import com.example.flourish.ui.navigation.NavigationRoute

@Composable
fun ExercisesScreen(
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        // Titolo in alto
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mindfulness Exercises",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }

        // Card centrate verticalmente
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ExerciseCard(
                iconActivity = R.drawable.exercises_breathing,
                activityName = "Breathing",
                onClick = { navController.navigate(NavigationRoute.Breathing.route) }
            )

//            Spacer(modifier = Modifier.height(32.dp))
//
//            ExerciseCard(
//                iconActivity = R.drawable.exercises_meditation,
//                activityName = "Meditation",
//                onClick = { navController.navigate(NavigationRoute.Meditation.route) }
//            )
        }
    }
}
