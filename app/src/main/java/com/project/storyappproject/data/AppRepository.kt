package com.project.storyappproject.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.project.storyappproject.data.model.User
import com.project.storyappproject.data.response.LoginResponse
import com.project.storyappproject.data.response.RegisterResponse
import com.project.storyappproject.data.retrofit.ApiService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AppRepository(
    private val apiService: ApiService,
    private val pref: UserPreferences
) {


    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isAddLoading = MutableLiveData<Boolean>()
    val isAddLoading: LiveData<Boolean> = _isAddLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _registerResponse = MutableLiveData<RegisterResponse> ()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    fun userLogin(email: String, password: String) {
        _isLoading.value = true
        val client = apiService.postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                try {
                    if (response.isSuccessful) {
                        _loginResponse.value = response.body()
                    } else {
                        val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                        _message.value = jsonObject?.getString("message")
                    }
                } catch (e: HttpException) {
                    Log.e(TAG, "onFailure: $e")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun userRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        val client = apiService.postRegister(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                try {
                    if (response.isSuccessful) {
                        _registerResponse.value = response.body()
                        _message.value = response.body()?.message!!
                    } else {
                        val jObject = response.errorBody()?.string()?.let { JSONObject(it) }
                        _message.value = jObject?.getString("message")
                    }
                } catch (e: HttpException) {
                    Log.e(TAG, "onFailure: $e")
                }

            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }


    suspend fun saveUserPref(user: User) {
        pref.saveUser(user)
    }


    suspend fun logout() {
        _loginResponse.value = null
        pref.logout()
    }

    fun getUserPref(): LiveData<User> {
        _message.value = ""
        return pref.getUser().asLiveData()
    }

    companion object {
        private const val  TAG = "AppRepository"

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(
            apiService: ApiService,
            pref: UserPreferences,
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService, pref)
            }.also { instance = it }
    }
}