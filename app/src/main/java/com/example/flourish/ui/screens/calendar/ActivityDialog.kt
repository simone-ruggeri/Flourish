package com.example.flourish.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.flourish.R
import com.example.flourish.viewmodel.ActivityDialogViewModel

data class ActivityItem(
    val name: String,
    val waterDrops: Int,
    val icon: Int
)


@Composable
fun ActivityDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    viewModel: ActivityDialogViewModel
) {
    var selectedActivity by remember { mutableStateOf<ActivityItem?>(null) }
    var time by remember { mutableStateOf("") }

    val activities = listOf(
        ActivityItem("Running", 10, R.drawable.activity_running),
        ActivityItem("Meditation", 8, R.drawable.activity_meditation),
        ActivityItem("Reading", 7, R.drawable.activity_reading)
    )

    val saveResult by viewModel.saveResult.collectAsState()

    // Quando salvataggio è successo, chiudo dialog e resetto stato
    LaunchedEffect(saveResult) {
        when (saveResult) {
            true -> {
                onDismiss()
                viewModel.resetSaveResult()
            }
            false -> {
                // Qui potresti mostrare un messaggio di errore
                viewModel.resetSaveResult()
            }
            null -> Unit
        }
    }

    LaunchedEffect(showDialog) {
        if (showDialog) {
            selectedActivity = null
            time = ""
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "New Activity",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 200.dp) // Scroll se troppe attività
                        .wrapContentSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(activities) { activity ->
                        ActivityCard(
                            iconActivity = activity.icon,
                            activityName = activity.name,
                            waterDrops = activity.waterDrops,
                            selected = (selectedActivity == activity),
                            modifier = Modifier.clickable { selectedActivity = activity }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = time,
                    onValueChange = {input ->
                        // Consenti solo numeri
                        if (input.all { it.isDigit() }) time = input
                    },
                    label = {
                        Text(
                            text = "Time (minutes)",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = "timer-icon",
                            Modifier.size(16.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    TextButton(onClick = {
                        viewModel.onAddClicked(selectedActivity, time)
                    }) {
                        Text(
                            text = "Add",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
