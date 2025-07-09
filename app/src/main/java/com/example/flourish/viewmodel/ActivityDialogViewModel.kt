package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.model.UserActivity
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.UserActivityRepository
import com.example.flourish.ui.screens.calendar.ActivityItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ActivityDialogViewModel(
    private val repository: UserActivityRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _userId = MutableStateFlow<Long?>(null)
    val userId = _userId.asStateFlow()

    private val _saveResult = MutableStateFlow<Boolean?>(null)
    val saveResult = _saveResult.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _activities = MutableStateFlow<List<UserActivity>>(emptyList())
    val activities = _activities.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.userIdFlow.collect {
                _userId.value = it
                loadActivities()
            }
        }

        viewModelScope.launch {
            _selectedDate.collect {
                loadActivities()
            }
        }
    }

    private suspend fun loadActivities() {
        val userId = _userId.value
        val date = _selectedDate.value
        if (userId != null) {
            val formattedDate = date.format(DateTimeFormatter.ISO_DATE)
            val list = repository.getActivitiesByDate(userId, formattedDate)
            _activities.value = list
        } else {
            _activities.value = emptyList()
        }
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onAddClicked(selectedActivity: ActivityItem?, timeInput: String) {
        val minutes = timeInput.toIntOrNull() ?: 0
        if (selectedActivity == null || minutes <= 0) {
            _saveResult.value = false // errore di validazione
            return
        }

        val currentUserId = _userId.value
        if (currentUserId == null) {
            _saveResult.value = false // errore userId mancante
            return
        }

        viewModelScope.launch {
            val formattedSelectedDate = _selectedDate.value.format(DateTimeFormatter.ISO_DATE)
            val userActivity = UserActivity(
                userId = currentUserId,
                date = formattedSelectedDate,
                activityName = selectedActivity.name,
                iconRes = selectedActivity.icon,
                minutes = minutes,
                waterDrops = selectedActivity.waterDrops
            )
            repository.insertActivity(userActivity)
            loadActivities()
            // Segnalo successo nel main thread
            _saveResult.value = true
        }
    }

    // Per "resettare" lo stato dopo aver reagito (utile nel Composable)
    fun resetSaveResult() {
        _saveResult.value = null
    }
}