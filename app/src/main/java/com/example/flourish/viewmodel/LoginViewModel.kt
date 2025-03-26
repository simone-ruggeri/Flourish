package com.example.flourish.viewmodel

import androidx.lifecycle.ViewModel
import com.example.flourish.data.repository.UserRepository

class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {

}