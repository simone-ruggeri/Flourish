package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearUserId()

            // Segnala alla UI che il logout è avvenuto
            _logoutEvent.value = true
        }
    }
}
