package com.example.flourish.ui.screens.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flourish.R
import com.example.flourish.viewmodel.ActivityDialogViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarScreen(viewModel: ActivityDialogViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    val selectedDate by viewModel.selectedDate.collectAsState()
    val activities by viewModel.activities.collectAsState()

    val currentMonthYear = remember(selectedDate) {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
        selectedDate.format(formatter)
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Wellness Diary",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = currentMonthYear,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            WeeklyCalendar(
                selectedDate = selectedDate,
                onDateSelected = { date -> viewModel.setSelectedDate(date) }
            )

            Spacer(modifier = Modifier.height(48.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(activities) { activity ->
                    ActivityCard(
                        iconActivity = activity.iconRes,
                        activityName = activity.activityName,
                        minutes = activity.minutes,
                        waterDrops = activity.waterDrops
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        ExtendedFloatingActionButton(
            icon = {
                   Icon(
                       painter = painterResource(id = R.drawable.plus),
                       contentDescription = "",
                       tint = Color.Unspecified
                   )
            },
            text = {
                Text(
                    text = "Add Activity",
                    color = MaterialTheme.colorScheme.onSurface
                ) },
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = false
                ),
            containerColor = MaterialTheme.colorScheme.surface,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 6.dp
            )
        )

        ActivityDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            viewModel = viewModel
        )
    }
}