package com.example.flourish.ui.screens.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.flourish.R
import com.example.flourish.ui.navigation.NavigationRoute
import com.example.flourish.viewmodel.SleepRatingViewModel

@Composable
fun SleepScreen(
    navController: NavHostController,
    viewModel: SleepRatingViewModel
) {

    val hasRatedToday by viewModel.hasRatedToday.collectAsState()
    val selectedMood = remember { mutableStateOf<Pair<Int, String>?>(null) }

    // Lista di tutte le icone con il relativo messaggio
    val moodIcons = listOf(
        R.drawable.mood_depressed to "2-4 hours",
        R.drawable.mood_sad to "4-5 hours",
        R.drawable.mood_neutral to "5-6 hours",
        R.drawable.mood_happy to "6-7 hours",
        R.drawable.mood_overjoyed to "7-9 hours"
    )

    LaunchedEffect(hasRatedToday) {
        if (hasRatedToday == true) {
            navController.navigate(NavigationRoute.Mood.route) {
                popUpTo(NavigationRoute.Sleep.route) { inclusive = true }
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "How would you rate your sleep quality?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(64.dp))

            // Placeholder o Icona Selezionata
            Box(
                modifier = Modifier
                    .size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                selectedMood.value?.let { (icon, description) ->
                    // Mostra l'icona selezionata
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "Selected Icon",
                            modifier = Modifier.size(180.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Timer, 
                                contentDescription = "timer-icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )   
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Row con tutte le icone cliccabili
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                moodIcons.forEach { (iconId, description) ->
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = description,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable {
                                // Aggiorna l'icona selezionata
                                selectedMood.value = iconId to description
                            },
                        tint = Color.Unspecified
                    )
                }
            }
        }

        // FAB in basso a destra
        FloatingActionButton(
            onClick = {
                selectedMood.value?.let { (_, description) ->
                    viewModel.saveSleepRating(description)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Check, contentDescription = "Save Icon")
        }
    }
}
