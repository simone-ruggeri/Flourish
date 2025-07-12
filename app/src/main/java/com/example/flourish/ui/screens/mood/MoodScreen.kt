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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Timer
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
import com.example.flourish.viewmodel.MoodRatingViewModel

data class MoodLevel(
    val rating: Int,
    val label: String,
    val iconRes: Int
)

@Composable
fun MoodScreen(
    navController: NavHostController,
    viewModel: MoodRatingViewModel
) {
    // State per tracciare l'icona selezionata
    val selectedMood = remember { mutableStateOf<MoodLevel?>(null) }

    val moodLevels = listOf(
        MoodLevel(1, "Very Bad", R.drawable.mood_depressed),
        MoodLevel(2, "Bad", R.drawable.mood_sad),
        MoodLevel(3, "Neutral", R.drawable.mood_neutral),
        MoodLevel(4, "Well", R.drawable.mood_happy),
        MoodLevel(5, "Excellent", R.drawable.mood_overjoyed)
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
                selectedMood.value?.let { mood ->
                    // Mostra l'icona selezionata
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = mood.iconRes),
                            contentDescription = mood.label,
                            modifier = Modifier.size(180.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = mood.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
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
                moodLevels.forEach { mood ->
                    Icon(
                        painter = painterResource(id = mood.iconRes),
                        contentDescription = mood.label,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable {
                                // Aggiorna l'icona selezionata
                                selectedMood.value = mood
                            },
                        tint = Color.Unspecified
                    )
                }
            }
        }

        // FAB in basso a destra
        FloatingActionButton(
            onClick = {
                selectedMood.value?.let { mood ->
                    viewModel.saveMoodRating(mood.rating) {
                        navController.navigate(NavigationRoute.Homepage.route) {
                            popUpTo(NavigationRoute.Mood.route) { inclusive = true }
                        }
                    }
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