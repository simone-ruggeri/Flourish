package com.example.flourish.ui.screens.mood

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun MoodScreen(
    navController: NavHostController
) {
    // State per tracciare l'icona selezionata
    val selectedMood = remember { mutableStateOf<Pair<Int, String>?>(null) }

    // Lista di tutte le icone con il relativo messaggio
    val moodIcons = listOf(
        R.drawable.mood_depressed to "Very Bad",
        R.drawable.mood_sad to "Bad",
        R.drawable.mood_neutral to "Neutral",
        R.drawable.mood_happy to "Well",
        R.drawable.mood_overjoyed to "Excellent"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "How would you describe your mood?",
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
                selectedMood.value?.let { (icon) ->
                    // Mostra l'icona selezionata
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "Selected Icon",
                            modifier = Modifier.size(180.dp),
                            tint = Color.Unspecified
                        )
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
                navController.navigate(NavigationRoute.Mood.route)
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