package com.project.storyappproject.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.storyappproject.data.AppRepository
import com.project.storyappproject.data.model.User
import com.project.storyappproject.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: AppRepository) : ViewModel() {

    val loginResponse: LiveData<LoginResponse?> = storyRepository.loginResponse
    val isLoading: LiveData<Boolean> = storyRepository.isLoading
    val message: LiveData<String> = storyRepository.message

    val getUser = storyRepository.getUserPref()

    fun userLogin(email: String, password: String) {
        viewModelScope.launch {
            storyRepository.userLogin(email, password)
        }
    }

    fun saveUserPref(user: User) {
        viewModelScope.launch {
            storyRepository.saveUserPref(user)
        }
    }
}