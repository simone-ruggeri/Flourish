package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.model.MoodRating
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.MoodRatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class MoodRatingViewModel(
    private val repository: MoodRatingRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _userId = MutableStateFlow<Long?>(null)
    val userId: StateFlow<Long?> = _userId.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.userIdFlow.collectLatest { id ->
                _userId.value = id
            }
        }
    }

    fun saveMoodRating(rating: Int, onSaved: () -> Unit) {
        viewModelScope.launch {
            val currentUserId = _userId.value
            if (currentUserId != null) {
                val now = LocalDateTime.now()
                val formattedDate = now.toLocalDate().toString() + " " + now.toLocalTime().truncatedTo(
                    ChronoUnit.MINUTES).toString()
                repository.insert(MoodRating(date = formattedDate, rating = rating, userId = currentUserId))
                onSaved()
            }
        }
    }
}
