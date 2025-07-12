package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.model.SleepRating
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.SleepRatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class SleepRatingViewModel(
    private val repository: SleepRatingRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _hasRatedToday = MutableStateFlow<Boolean?>(null)
    val hasRatedToday: StateFlow<Boolean?> = _hasRatedToday.asStateFlow()

    private val _userId = MutableStateFlow<Long?>(null)
    val userId = _userId.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.userIdFlow.collectLatest { id ->
                _userId.value = id
                if (id != null) {
                    val today = LocalDate.now().toString()
                    val rating = repository.getSleepRatingByDateAndUser(today, id)
                    _hasRatedToday.value = rating != null
                } else {
                    _hasRatedToday.value = false
                }
            }
        }
    }

    fun saveSleepRating(rating: Int) {
        viewModelScope.launch {
            val currentUserId = _userId.value
            if (currentUserId != null) {
                val today = LocalDate.now().toString()
                repository.insert(SleepRating(date = today, rating = rating, userId = currentUserId))
                _hasRatedToday.value = true
            }
        }
    }

}