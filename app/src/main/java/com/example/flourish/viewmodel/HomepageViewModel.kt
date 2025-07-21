package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.PlantRepository
import com.example.flourish.data.repository.UserActivityRepository
import com.example.flourish.ui.screens.homepage.plantDrawables
import kotlinx.coroutines.delay
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
    private val userActivityRepository: UserActivityRepository,
    private val plantRepository: PlantRepository
) : ViewModel() {
    private val _userId = MutableStateFlow<Long?>(null)
    val userId = _userId.asStateFlow()

    private val _weeklyWaterDrops = MutableStateFlow(0)
    val weeklyWaterDrops: StateFlow<Int> = _weeklyWaterDrops

    private val _plantStage = MutableStateFlow(0)
    val plantStage: StateFlow<Int> = _plantStage

    private val _plantHealth = MutableStateFlow("healthy")
    val plantHealth: StateFlow<String> = _plantHealth

    private val survivalThreshold = 40
    private val growthThreshold = 50

    private val _showTransition = MutableStateFlow(false)
    val showTransition: StateFlow<Boolean> = _showTransition

    private var lastStage = 0

    init {
        viewModelScope.launch {
            userPreferences.userIdFlow.collect {
                _userId.value = it
                loadWeeklyWaterDrops()
                observePlantStage()
                loadPlantStatus()
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
            val total = userActivityRepository.getWeeklyWaterDrops(id, startDate, endDate)
            _weeklyWaterDrops.value = total
            updateDailyHealth()
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

    private fun loadPlantStatus() {
        viewModelScope.launch {
            userPreferences.plantStageFlow.collect {
                _plantStage.value = it
            }
            userPreferences.plantHealthFlow.collect {
                _plantHealth.value = it
            }
        }
    }

    private fun updateDailyHealth() {
        val drops = weeklyWaterDrops.value
        val today = LocalDate.now().dayOfWeek.value
        val expectedProgress = (survivalThreshold / 7.0) * today

        val health = when {
            drops >= expectedProgress -> "healthy"
            drops >= expectedProgress * 0.6 -> "struggling"
            else -> "wilted"
        }

        viewModelScope.launch {
            userPreferences.savePlantHealth(health)
            _plantHealth.value = health
            val user = userId.value ?: return@launch
            plantRepository.insertOrUpdatePlant(user, plantStage.value, plantHealth.value)
        }
    }

    fun updateWeeklyStage() {
        val drops = weeklyWaterDrops.value
        val currentStage = plantStage.value
        val maxStage = plantDrawables.keys.maxOfOrNull { it.first } ?: 4

        if (drops >= growthThreshold && currentStage < maxStage) {
            val newStage = currentStage + 1
            viewModelScope.launch {
                val user = userId.value ?: return@launch
                userPreferences.savePlantStage(newStage)
                _plantStage.value = newStage
                plantRepository.insertOrUpdatePlant(user, newStage, plantHealth.value)
            }
        }
    }

    private fun observePlantStage() {
        viewModelScope.launch {
            userPreferences.plantStageFlow.collect { stage ->
                if (stage > lastStage) {
                    _showTransition.value = true
                    delay(2000) // Tempo della transizione (es. 2 secondi)
                    _showTransition.value = false
                }
                _plantStage.value = stage
                lastStage = stage
            }
        }
    }

    fun setShowTransition(value: Boolean) {
        _showTransition.value = value
    }

    fun setPlantStage(stage: Int) {
        _plantStage.value = stage
    }

    fun setPlantHealth(health: String) {
        _plantHealth.value = health
    }


}