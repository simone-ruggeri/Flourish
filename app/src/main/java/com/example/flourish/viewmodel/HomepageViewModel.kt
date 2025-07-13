package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.UserActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
}