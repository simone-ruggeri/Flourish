package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.MoodRatingRepository
import com.example.flourish.data.repository.SleepRatingRepository
import com.example.flourish.data.repository.UserActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ActivityDistribution(
    val activityType: String,
    val duration: Int // o count
)

data class MoodDistribution(
    val timestamp: String,
    val rating: Int
)

data class SleepDistribution(
    val date: String,
    val rating: Int
)


class ChartViewModel(
    private val sleepRepository: SleepRatingRepository,
    private val moodRepository: MoodRatingRepository,
    private val activityRepository: UserActivityRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _userId = MutableStateFlow<Long?>(null)
    val userId = _userId.asStateFlow()

    val weeklyActivityDistribution = MutableStateFlow<List<ActivityDistribution>>(emptyList())
    val weeklyMood = MutableStateFlow<List<MoodDistribution>>(emptyList())
    val weeklySleep = MutableStateFlow<List<SleepDistribution>>(emptyList())


    init {
        viewModelScope.launch {
            userPreferences.userIdFlow.collectLatest { id ->
                _userId.value = id
            }
        }
    }

    private fun getCurrentWeekDates(): List<String> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()
        val monday = today.with(DayOfWeek.MONDAY)
        return (0..6).map { i -> monday.plusDays(i.toLong()).format(formatter) }
    }

    fun loadWeeklyActivities(userId: Long) {
        viewModelScope.launch {
            // Evita di ricaricare se già presenti dati
            if (weeklyActivityDistribution.value.isNotEmpty()) return@launch

            val dates = getCurrentWeekDates()
            val startDate = dates.first()
            val endDate = dates.last()

            val activities = activityRepository.getActivitiesForWeek(userId, startDate, endDate)

            val grouped = activities.groupBy { it.activityName }
                .map { (name, list) ->
                    ActivityDistribution(name, list.sumOf { it.minutes }) // o list.size se ti interessa la frequenza
                }

            weeklyActivityDistribution.value = grouped
        }
    }

    fun loadWeeklyMood(userId: Long) {
        viewModelScope.launch {
            if (weeklyMood.value.isNotEmpty()) return@launch

            val dates = getCurrentWeekDates()
            val startDate = dates.first()
            val endDate = dates.last()

            val moods = moodRepository.getMoodRatingsForWeek(userId, startDate, endDate)

            // Raggruppa per data e calcola media per ciascun giorno
            val moodByDay = moods.groupBy { it.date.substring(0, 10) }
                .mapValues { (_, entries) -> entries.map { it.rating }.average().toInt() }

            // Crea lista settimanale completa con 0 dove mancano dati
            val weeklyData = dates.map { date ->
                MoodDistribution(
                    timestamp = date,
                    rating = moodByDay[date] ?: 0
                )
            }

            weeklyMood.value = weeklyData
        }
    }


    fun loadWeeklySleep(userId: Long) {
        viewModelScope.launch {
            if (weeklySleep.value.isNotEmpty()) return@launch

            val dates = getCurrentWeekDates()
            val startDate = dates.first()
            val endDate = dates.last()

            val sleeps = sleepRepository.getSleepRatingsForWeek(userId, startDate, endDate)

            val sleepByDay = sleeps.associateBy { it.date } // Un solo valore al giorno

            val weeklyData = dates.map { date ->
                SleepDistribution(
                    date = date,
                    rating = sleepByDay[date]?.rating ?: 0
                )
            }

            weeklySleep.value = weeklyData
        }
    }

}