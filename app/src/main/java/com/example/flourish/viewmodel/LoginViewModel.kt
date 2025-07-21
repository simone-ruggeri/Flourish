package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.model.User
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.PlantRepository
import com.example.flourish.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val errorMessage: String? = null
)

class LoginViewModel(
    private val userRepository: UserRepository,
    private val plantRepository: PlantRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    private val _loginState = MutableStateFlow<Result<User?>?>(null)
    val loginState: StateFlow<Result<User?>?> = _loginState

    fun onEmailChanghed(newEmail: String) {
        _loginUiState.value = _loginUiState.value.copy(email = newEmail, emailError = null)
    }

    fun onPasswordChanged(newPassword: String) {
        _loginUiState.value = _loginUiState.value.copy(password = newPassword, passwordError = null)
    }

    fun loginUser() {
        val state = _loginUiState.value
        if (state.email.isBlank()) {
            _loginUiState.value = state.copy(emailError = "Email cannot be empty")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _loginUiState.value = state.copy(emailError = "Invalid email format")
            return
        }
        if (state.password.isBlank()) {
            _loginUiState.value = state.copy(passwordError = "Password cannot be empty")
            return
        }
        _loginUiState.value = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = userRepository.loginUser(state.email, state.password)
            if (result.isSuccess && result.getOrNull() != null) {

                // Salva lo userId in UserPreferences
                val user = result.getOrNull()!!
                userPreferences.saveUserId(user.id)

                val plant = plantRepository.getPlantByUserId(user.id)

                if (plant != null) {
                    userPreferences.savePlantStage(plant.stage)
                    userPreferences.savePlantHealth(plant.health)
                } else {
                    // Se è un nuovo utente, imposta valori di default
                    userPreferences.savePlantStage(0)
                    userPreferences.savePlantHealth("healthy")
                    plantRepository.insertOrUpdatePlant(user.id, 0, "healthy")
                }

                _loginState.value = Result.success(result.getOrNull())
                _loginUiState.value = state.copy(
                    isLoading = false,
                    errorMessage = null
                )
            } else {
                _loginState.value = result
                _loginUiState.value =state.copy(
                    isLoading = false,
                    errorMessage = "Invalid Credentials"
                )
            }
        }
    }
}