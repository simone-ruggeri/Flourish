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
import com.example.flourish.viewmodel.MoodDistribution
import com.example.flourish.viewmodel.SleepDistribution
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter


@Composable
fun ChartScreen(
    viewodel: ChartViewModel
) {
    val userId by viewodel.userId.collectAsState()

    val activityDist by viewodel.weeklyActivityDistribution.collectAsState()
    val moodRatings by viewodel.weeklyMood.collectAsState()
    val sleepRatings by viewodel.weeklySleep.collectAsState()

    LaunchedEffect(userId) {
        userId?.let {
            viewodel.loadWeeklyActivities(it)
            viewodel.loadWeeklyMood(it)
            viewodel.loadWeeklySleep(it)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Activity Distribution",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            if (activityDist.isNotEmpty()) {
                ActivityPieChart(activityDistribution = activityDist)
            } else {
                EmptyChart()
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Sleep Pattern",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            if (sleepRatings.any { it.rating != 0 }) {
                SleepBarChart(sleepRatings)
            } else {
                EmptyChart()
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Mood Trends",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            if (moodRatings.any { it.rating != 0 }) {
                MoodBarChart(moodRatings)
            } else {
                EmptyChart()
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
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
fun SleepBarChart(sleepRatings: List<SleepDistribution>) {
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val barColor = MaterialTheme.colorScheme.tertiary.toArgb()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context -> BarChart(context) },
            update = { chart ->
                val entries = sleepRatings.mapIndexed { index, sleep ->
                    BarEntry(index.toFloat(), sleep.rating.toFloat())
                }

                /*val dataSet = BarDataSet(entries, "Sleep Quality").apply {
                    color = barColor
                    valueTextColor = textColor
                    valueTextSize = 12f
                }*/
                val dataSet = BarDataSet(entries, "Sleep Quality").apply {
                    color = barColor
                    valueTextColor = textColor
                    valueTextSize = 18f
                    valueFormatter = object : ValueFormatter() {
                        override fun getBarLabel(barEntry: BarEntry?): String {
                            return when (barEntry?.y?.toInt()) {
                                1 -> "😞"
                                2 -> "😟"
                                3 -> "😐"
                                4 -> "🙂"
                                5 -> "😄"
                                else -> ""
                            }
                        }
                    }
                }


                chart.data = BarData(dataSet)

                val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val labels = daysOfWeek.take(sleepRatings.size)

                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    setTextColor(textColor)
                    setLabelCount(labels.size, false)
                    axisMinimum = -0.5f
                    axisMaximum = labels.size - 0.5f
                }

                chart.axisLeft.apply {
                    axisMinimum = 0f
                    granularity = 1f
                    setDrawGridLines(true)
                    setTextColor(textColor)
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            //  return if (value % 1f == 0f) value.toInt().toString() else ""
                            return when (value.toInt()) {
                                1 -> "😞"
                                2 -> "😟"
                                3 -> "😐"
                                4 -> "🙂"
                                5 -> "😄"
                                else -> ""
                            }
                        }
                    }
                }

                chart.axisRight.isEnabled = false
                chart.legend.textColor = textColor
                chart.description.isEnabled = false

                chart.invalidate()
            }
        )
    }
}

@Composable
fun MoodBarChart(moodRatings: List<MoodDistribution>) {
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val barColor = MaterialTheme.colorScheme.secondary.toArgb()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context -> BarChart(context) },
            update = { chart ->
                val entries = moodRatings.mapIndexed { index, mood ->
                    BarEntry(index.toFloat(), mood.rating.toFloat())
                }

                /*val dataSet = BarDataSet(entries, "Mood Average").apply {
                    color = barColor
                    valueTextColor = textColor
                    valueTextSize = 12f
                }*/
                val dataSet = BarDataSet(entries, "Mood Average").apply {
                    color = barColor
                    valueTextColor = textColor
                    valueTextSize = 18f
                    valueFormatter = object : ValueFormatter() {
                        override fun getBarLabel(barEntry: BarEntry?): String {
                            return when (barEntry?.y?.toInt()) {
                                1 -> "😞"
                                2 -> "😟"
                                3 -> "😐"
                                4 -> "🙂"
                                5 -> "😄"
                                else -> ""
                            }
                        }
                    }
                }


                chart.data = BarData(dataSet)

                val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

                val labels = daysOfWeek.take(moodRatings.size)

                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    setTextColor(textColor)
                    setLabelCount(labels.size, false)
                    axisMinimum = -0.5f
                    axisMaximum = labels.size - 0.5f
                }

                chart.axisLeft.apply {
                    axisMinimum = 0f
                    granularity = 1f
                    setDrawGridLines(true)
                    setTextColor(textColor)
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            // return if (value % 1f == 0f) value.toInt().toString() else ""
                            return when (value.toInt()) {
                                1 -> "😞"
                                2 -> "😟"
                                3 -> "😐"
                                4 -> "🙂"
                                5 -> "😄"
                                else -> ""
                            }
                        }
                    }
                }

                chart.axisRight.isEnabled = false
                chart.legend.textColor = textColor
                chart.description.isEnabled = false

                chart.invalidate()
            }
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
                text = "Chart not available.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No data entered yet for this week",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}