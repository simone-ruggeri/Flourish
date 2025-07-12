package com.example.flourish.ui.screens.chart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.flourish.ui.theme.ActivityMeditationColor
import com.example.flourish.ui.theme.ActivityReadingColor
import com.example.flourish.ui.theme.ActivityRunningColor
import com.example.flourish.viewmodel.ActivityDistribution
import com.example.flourish.viewmodel.ChartViewModel
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.charts.PieChart



@Composable
fun ChartScreen(
    viewodel: ChartViewModel
) {
    val userId by viewodel.userId.collectAsState()

    val activityDist by viewodel.weeklyActivityDistribution.collectAsState()
    val moodRatings by viewodel.weeklyMood.collectAsState()
    val sleepRatings by viewodel.weeklySleep.collectAsState()

    LaunchedEffect(userId) {
        userId?.let { viewodel.loadWeeklyActivities(it) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Distribuzione Attività", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            if (activityDist.isNotEmpty()) {
                ActivityPieChart(activityDistribution = activityDist)
            } else {
                EmptyChart()
            }
        }
    }
}

@Composable
fun ActivityPieChart(activityDistribution: List<ActivityDistribution>) {
    // Converti i dati in Entry per il PieChart
    val entries = activityDistribution.map {
        PieEntry(it.duration.toFloat(), it.activityType)
    }

    val activityColorMap = mapOf(
        "Running" to ActivityRunningColor.toArgb(),
        "Meditation" to ActivityMeditationColor.toArgb(),
        "Reading" to ActivityReadingColor.toArgb()
    )

    // Configura il dataset
    val dataSet = PieDataSet(entries, "").apply {
        colors = entries.map { entry ->
            activityColorMap[entry.label] ?: Color.Gray.toArgb()
        }
        valueTextColor = Color.Black.toArgb()
        valueTextSize = 12f
    }

    val holeColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    // Crea i dati per il grafico
    val pieData = PieData(dataSet)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    data = pieData
                    description.isEnabled = false
                    legend.isEnabled = true
                    setUsePercentValues(true)
                    setEntryLabelColor(textColor.toArgb())
                    setEntryLabelTextSize(14f)
                    setHoleColor(holeColor.toArgb())
                    legend.textColor = textColor.toArgb()
                    isDrawHoleEnabled = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Composable
fun EmptyChart() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Charts not available.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No routes completed yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}