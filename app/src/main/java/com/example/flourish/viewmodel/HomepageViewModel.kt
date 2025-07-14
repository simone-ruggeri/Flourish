package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.UserActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomepageViewModel (
    private val userPreferences: UserPreferences,
    private val repository: UserActivityRepository
) : ViewModel() {
    private val _userId = MutableStateFlow<Long?>(null)
    val userId = _userId.asStateFlow()

    private val _weeklyWaterDrops = MutableStateFlow(0)
    val weeklyWaterDrops: StateFlow<Int> = _weeklyWaterDrops

    private val survivalThreshold = 40
    private val growthThreshold = 50

    init {
        viewModelScope.launch {
            userPreferences.userIdFlow.collect {
                _userId.value = it
                loadWeeklyWaterDrops()
            }
        }
    }

    private fun getCurrentWeekRange(): Pair<String, String> {
        val today = LocalDate.now()
        val startOfWeek = today.with(DayOfWeek.MONDAY)
        val endOfWeek = today.with(DayOfWeek.SUNDAY)

        val formatter = DateTimeFormatter.ISO_DATE
        return Pair(startOfWeek.format(formatter), endOfWeek.format(formatter))
    }

    private fun loadWeeklyWaterDrops() {
        val (startDate, endDate) = getCurrentWeekRange()

        viewModelScope.launch {
            val id = _userId.value ?: return@launch
            val total = repository.getWeeklyWaterDrops(id, startDate, endDate)
            _weeklyWaterDrops.value = total
        }
    }

    val plantStatus: StateFlow<String> = weeklyWaterDrops.map { drops ->
        val today = LocalDate.now().dayOfWeek.value // 1 = Monday, 7 = Sunday
        val dailyTarget = survivalThreshold / 7.0
        val expectedProgress = dailyTarget * today

        when {
            drops >= growthThreshold -> "Your plant is thriving and growing!"
            drops >= survivalThreshold -> "Your plant is healthy and surviving!"
            drops >= expectedProgress -> "Your plant is on track to survive, keep going!"
            drops >= expectedProgress * 0.6 -> "Your plant is struggling, give it more care!"
            else -> "Your plant is wilting, it really needs attention!"
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "Loading plant status...")
}