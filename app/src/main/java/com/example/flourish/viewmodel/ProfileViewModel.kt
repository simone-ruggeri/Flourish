package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class ProfileUiState(
    val id: Long = 0L,
    val name: String = "",
    val email: String = "",
    val profileImageUri: String = ""
)

class ProfileViewModel(
    private val userPreferences: UserPreferences,
    private val repository: UserRepository
) : ViewModel() {

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent

    private val _profileState = MutableStateFlow(ProfileUiState(id = 0L))
    val profileState: StateFlow<ProfileUiState> = _profileState

    fun loadUser() {
        viewModelScope.launch {
            val userId = userPreferences.userIdFlow.firstOrNull()
            if (userId != null) {
                val user = repository.getUserById(userId)
                user?.let {
                    // Imposta i dati di ProfileUiState
                    _profileState.value = ProfileUiState(
                        id = it.id,
                        name = "${it.firstName} ${it.lastName}",
                        email = it.email,
                        profileImageUri = it.profileImagePath ?: ""
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearUserId()

            // Segnala alla UI che il logout è avvenuto
            _logoutEvent.value = true
        }
    }

    fun updateProfileImage(imagePath: String) {
        viewModelScope.launch {
            val userId = userPreferences.userIdFlow.firstOrNull()
            if (userId != null) {
                repository.updateUserProfileImage(userId, imagePath)
                // Aggiorna l'utente corrente
                _profileState.value = _profileState.value.copy(profileImageUri = imagePath)
            }
        }
    }

    fun removeProfileImage() {
        viewModelScope.launch {
            val userId = userPreferences.userIdFlow.firstOrNull()
            if (userId != null) {
                repository.updateUserProfileImage(userId, "") // Pulisce il campo
                _profileState.value = _profileState.value.copy(profileImageUri = "")
            }
        }
    }
}
