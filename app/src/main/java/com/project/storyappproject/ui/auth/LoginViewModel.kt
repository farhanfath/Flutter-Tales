package com.project.storyappproject.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.storyappproject.data.api.ApiConfig
import com.project.storyappproject.data.model.response.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess : LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _loginResult = MutableLiveData<AuthResponse>()
    val loginResult : LiveData<AuthResponse> = _loginResult

    fun userLogin(email: String, password: String) {
        _isLoading.value = true
        _isError.value = false
        _isSuccess.value = false

        val service = ApiConfig().getApiService().postLogin(email, password)
        service.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.body()?.error == false) {
                    _loginResult.value = response.body()
                    _isError.value = false
                } else {
                    _isError.value = true
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}