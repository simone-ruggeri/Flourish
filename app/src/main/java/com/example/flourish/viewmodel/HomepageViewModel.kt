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
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields

class HomepageViewModel(
    private val userPreferences: UserPreferences,
    private val userActivityRepository: UserActivityRepository,
    private val plantRepository: PlantRepository
) : ViewModel() {

    private val survivalThreshold = 40
    private val growthThreshold = 50
    private var lastStage = 0
    private var firstUseDate: LocalDate? = null

    private val _userId = MutableStateFlow<Long?>(null)
    val userId = _userId.asStateFlow()

    private val _weeklyWaterDrops = MutableStateFlow(0)
    val weeklyWaterDrops: StateFlow<Int> = _weeklyWaterDrops

    private val _plantStage = MutableStateFlow(0)
    val plantStage: StateFlow<Int> = _plantStage

    private val _plantHealth = MutableStateFlow("healthy")
    val plantHealth: StateFlow<String> = _plantHealth

    private val _showTransition = MutableStateFlow(false)
    val showTransition: StateFlow<Boolean> = _showTransition

    init {
        viewModelScope.launch {
            // 1) Carica o salva la data di prima apertura
            val savedDate = userPreferences.firstUseDateFlow.firstOrNull()
            if (savedDate == null) {
                val today = LocalDate.now()
                userPreferences.saveFirstUseDate(today)
                firstUseDate = today
            } else {
                firstUseDate = savedDate
            }

            // 2) Inizializza plantStage da DataStore
            val savedStage = userPreferences.plantStageFlow.firstOrNull() ?: 0
            _plantStage.value = savedStage
            lastStage = savedStage

            // 3) Inizializza health da DataStore
            userPreferences.plantHealthFlow.collect { health ->
                _plantHealth.value = health
            }
        }

        // 4) Osserva cambi di stage per animazioni
        viewModelScope.launch {
            userPreferences.plantStageFlow.drop(1).collect { newStage ->
                if (newStage > lastStage) {
                    _showTransition.value = true
                    delay(2000)
                    _showTransition.value = false
                }
                _plantStage.value = newStage
                lastStage = newStage
            }
        }

        // 5) Osserva userId per caricare dati e gestire settimana
        viewModelScope.launch {
            userPreferences.userIdFlow.collect { id ->
                _userId.value = id
                loadWeeklyWaterDrops()
                handleWeeklyPlantUpdate()
            }
        }
    }

    // PLANT STATUS

    val plantStatus: StateFlow<String> = weeklyWaterDrops.map { drops ->
        val days = getEffectiveDayCount()
        val dailyTarget = survivalThreshold / 7.0
        val expectedProgress = dailyTarget * days

        when {
            drops >= growthThreshold -> "Your plant is thriving and growing!"
            drops >= survivalThreshold -> "Your plant is healthy and surviving!"
            drops >= expectedProgress -> "Your plant is on track to survive, keep going!"
            drops >= expectedProgress * 0.6 -> "Your plant is struggling, give it more care!"
            else -> "Your plant is wilting, it really needs attention!"
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "Loading plant status...")

    private fun loadWeeklyWaterDrops() {
        val (startDate, endDate) = getCurrentWeekRange()
        viewModelScope.launch {
            val id = _userId.value ?: return@launch
            val total = userActivityRepository.getWeeklyWaterDrops(id, startDate, endDate)
            _weeklyWaterDrops.value = total
            updateDailyHealth()
        }
    }

    private fun updateDailyHealth() {
        val drops = weeklyWaterDrops.value
        val days = getEffectiveDayCount()
        val expectedProgress = (survivalThreshold / 7.0) * days

        val health = when {
            drops >= expectedProgress -> "healthy"
            drops >= expectedProgress * 0.6 -> "struggling"
            else -> "wilted"
        }

        viewModelScope.launch {
            userPreferences.savePlantHealth(health)
            _plantHealth.value = health
            val user = userId.value ?: return@launch
            plantRepository.insertOrUpdatePlant(user, plantStage.value, health)
        }
    }

    // WEEKLY STAGE UPDATE

    // Gestione cambio settimana e crescita della pianta
    fun handleWeeklyPlantUpdate() {
        viewModelScope.launch {
            val lastUpdatedWeek = userPreferences.weekUpdatedFlow.firstOrNull()
            val previousWeek     = getPreviousWeekIdentifier()

            if (lastUpdatedWeek != previousWeek) {
                updatePlantStageForPreviousWeek()
                userPreferences.saveWeekUpdated(previousWeek)
            }
        }
    }

    private fun getEffectiveDayCount(): Int {
        val today = LocalDate.now()
        val monday = today.with(DayOfWeek.MONDAY)
        return if (firstUseDate != null && firstUseDate!!.isAfter(monday)) {
            ChronoUnit.DAYS.between(firstUseDate, today).toInt() + 1
        } else {
            today.dayOfWeek.value
        }
    }

//    private suspend fun getLastUpdatedWeek(): String? {
//        return userPreferences.weekUpdatedFlow.firstOrNull()
//    }

    private fun getPreviousWeekIdentifier(): String {
        val previousWeekDate = LocalDate.now().minusWeeks(1)
        val week = previousWeekDate.get(WeekFields.ISO.weekOfWeekBasedYear())
        val year = previousWeekDate.year
        return "$year-W$week"
    }

    private fun getPreviousWeekDateRange(): Pair<String, String> {
        val previousMonday = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY)
        val previousSunday = LocalDate.now().minusWeeks(1).with(DayOfWeek.SUNDAY)
        val formatter = DateTimeFormatter.ISO_DATE
        return Pair(previousMonday.format(formatter), previousSunday.format(formatter))
    }

    private fun updatePlantStageForPreviousWeek() {
        val (startDate, endDate) = getPreviousWeekDateRange()
        viewModelScope.launch {
            val user = userId.value ?: return@launch
            val drops = _weeklyWaterDrops.value //userActivityRepository.getWeeklyWaterDrops(user, startDate, endDate)
            if (shouldGrowPlant(drops)) {
                incrementPlantStage(user)
            }
        }
    }

    private fun shouldGrowPlant(drops: Int): Boolean {
        val currentStage = plantStage.value
        val maxStage = plantDrawables.keys.maxOfOrNull { it.first } ?: 4
        return drops >= growthThreshold && currentStage < maxStage
    }

    private suspend fun incrementPlantStage(user: Long) {
        val newStage = plantStage.value + 1
        userPreferences.savePlantStage(newStage)
        _plantStage.value = newStage
        plantRepository.insertOrUpdatePlant(user, newStage, plantHealth.value)
    }

    // UTILITY

    private fun getCurrentWeekRange(): Pair<String, String> {
        val today = LocalDate.now()
        val startOfWeek = today.with(DayOfWeek.MONDAY)
        val endOfWeek = today.with(DayOfWeek.SUNDAY)
        val formatter = DateTimeFormatter.ISO_DATE
        return Pair(startOfWeek.format(formatter), endOfWeek.format(formatter))
    }

    // DEBUG
    private fun setShowTransition(value: Boolean) {
        _showTransition.value = value
    }

    private fun setPlantStage(stage: Int) {
        _plantStage.value = stage
    }

    private fun setPlantHealth(health: String) {
        _plantHealth.value = health
    }

    suspend fun showTransitions() {
        setPlantStage(0)
        setPlantHealth("healthy")
        delay(2000)
        setPlantHealth("struggling")
        delay(2000)
        setPlantHealth("wilted")
        delay(2000)
        setShowTransition(true)
        delay(2000)
        setShowTransition(false)

        setPlantStage(1)
        setPlantHealth("healthy")
        delay(2000)
        setPlantHealth("struggling")
        delay(2000)
        setPlantHealth("wilted")
        delay(2000)
        setShowTransition(true)
        delay(2000)
        setShowTransition(false)

        setPlantStage(2)
        setPlantHealth("healthy")
        delay(2000)
        setPlantHealth("struggling")
        delay(2000)
        setPlantHealth("wilted")
        delay(2000)
        setShowTransition(true)
        delay(2000)
        setShowTransition(false)

        setPlantStage(3)
        setPlantHealth("healthy")
        delay(2000)
        setPlantHealth("struggling")
        delay(2000)
        setPlantHealth("wilted")
        delay(2000)
        setShowTransition(true)
        delay(2000)
        setShowTransition(false)

        setPlantStage(4)
        setPlantHealth("healthy")
        delay(2000)
        setPlantHealth("struggling")
        delay(2000)
        setPlantHealth("wilted")
    }

    fun simulateWeeklyWaterDrops(drops: Int) {
        _weeklyWaterDrops.value = drops
    }

    fun simulateLastUpdatedWeek(weekIdentifier: String) {
        viewModelScope.launch {
            userPreferences.saveWeekUpdated(weekIdentifier)
        }
    }

}
