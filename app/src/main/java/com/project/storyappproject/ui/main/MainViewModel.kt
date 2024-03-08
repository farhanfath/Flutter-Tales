package com.project.storyappproject.ui.main

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.asLiveData
//import androidx.lifecycle.viewModelScope
//import com.project.storyappproject.data.UserPreferences
//import com.project.storyappproject.data.response.LoginResult
//import kotlinx.coroutines.launch
//
//
//class MainViewModel(private val pref: UserPreferences) : ViewModel() {
//    fun getUser(): LiveData<LoginResult> {
//        return pref.getUser().asLiveData()
//    }
//
//    fun logout() {
//        viewModelScope.launch {
//            pref.logout()
//        }
//    }
//}