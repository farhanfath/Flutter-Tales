package com.project.storyappproject.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.storyappproject.data.AppRepository
import com.project.storyappproject.data.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val appRepository: AppRepository) : ViewModel() {

    val registerResponse: LiveData<RegisterResponse> = appRepository.registerResponse
    val isLoading: LiveData<Boolean> = appRepository.isLoading
    val message: LiveData<String> = appRepository.message

    val getUser = appRepository.getUserPref()

    fun userRegister(name: String, email: String, password: String) {
        viewModelScope.launch {
            appRepository.userRegister(name, email, password)
        }
    }
}



