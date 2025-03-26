package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.model.User
import com.example.flourish.data.preferences.UserPreferences
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
    private val repository: UserRepository,
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
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _loginUiState.value = state.copy(emailError = "Invalid email format")
            return
        }
        if (state.password.isBlank()) {
            _loginUiState.value = state.copy(passwordError = "Password cannot be empty")
        }
        _loginUiState.value = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = repository.loginUser(state.email, state.password)
            if (result.isSuccess && result.getOrNull() != null) {

                // Salva lo userId in UserPreferences
                val user = result.getOrNull()!!
                userPreferences.saveUserId(user.id)

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