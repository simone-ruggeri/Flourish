package com.example.flourish.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WeeklyCalendar() {
    val daysOfWeek = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
    val dayNumbers = listOf(25, 26, 27, 28, 29, 1, 2) // Esempio di giorni

    var selectedDay by remember { mutableStateOf<Int?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Combina i due elenchi in coppie (giorno, numero)
        daysOfWeek.zip(dayNumbers).forEach { (day, number) ->

            // Ogni elemento è una colonna (giorno sopra e numero sotto)
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { selectedDay = number }
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            clip = false // Lascia l'ombra fuori dal contenuto
                        ),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedDay == number) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                ) {
                    // Box per centrare il numero all'interno del cerchio
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = number.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedDay == number) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
