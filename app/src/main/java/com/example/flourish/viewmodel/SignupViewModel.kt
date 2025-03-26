package com.example.flourish.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.data.model.User
import com.example.flourish.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegistrationUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val errorMessage: String? = null
)

class SignupViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _registrationUiState = MutableStateFlow(RegistrationUiState())
    val registrationUiState: StateFlow<RegistrationUiState> = _registrationUiState

    private val _registrationState = MutableStateFlow<Result<Long?>?>(null)
    val registrationState: StateFlow<Result<Long?>?> = _registrationState

    fun onEmailChanged(newEmail: String) {
        _registrationUiState.value =
            _registrationUiState.value.copy(email = newEmail, emailError = null)
    }

    fun onPasswordChanged(newPassword: String) {
        _registrationUiState.value =
            _registrationUiState.value.copy(password = newPassword, passwordError = null)
    }

    fun onFirstNameChanged(newFirstName: String) {
        _registrationUiState.value =
            _registrationUiState.value.copy(firstName = newFirstName, firstNameError = null)
    }

    fun onLastNameChanged(newLastName: String) {
        _registrationUiState.value =
            _registrationUiState.value.copy(lastName = newLastName, lastNameError = null)
    }

    fun registerUser() {
        val state = _registrationUiState.value

        if (state.firstName.isBlank()) {
            _registrationUiState.value = state.copy(firstNameError = "First Name cannot be empty")
            return
        }
        if (state.lastName.isBlank()) {
            _registrationUiState.value = state.copy(lastNameError = "Last Name cannot be empty")
            return
        }
        if (state.email.isBlank()) {
            _registrationUiState.value = state.copy(emailError = "Email cannot be empty")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _registrationUiState.value = state.copy(emailError = "Invalid email format")
            return
        }
        if (state.password.isBlank()) {
            _registrationUiState.value = state.copy(passwordError = "Password cannot be empty")
            return
        }

        _registrationUiState.value = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val user = User(
                firstName = state.firstName,
                lastName = state.lastName,
                email = state.email,
                password = state.password
            )
            val result = repository.registerUser(user)
            if (result.isSuccess && result.getOrNull() != null) {
                _registrationState.value = Result.success(result.getOrNull())
                _registrationUiState.value = state.copy(
                    isLoading = false,
                    errorMessage = null
                )
            } else {
                _registrationUiState.value = state.copy(
                    isLoading = false,
                    emailError = "Email already in use",
                    errorMessage = "Registration failed."
                )
            }
        }
    }
}