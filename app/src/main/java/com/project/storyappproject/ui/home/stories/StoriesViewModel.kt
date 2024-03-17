package com.project.storyappproject.ui.home.stories

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.storyappproject.data.api.ApiConfig
import com.project.storyappproject.data.datastore.UserPreference
import com.project.storyappproject.data.model.response.ListStoryItem
import com.project.storyappproject.data.model.response.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel(application: Application) : AndroidViewModel(application) {
    private val context: Context
        get() = getApplication<Application>().applicationContext

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    init {
        getStories()
    }

     fun getStories() {
        val token = UserPreference(context).getUser().token
        _isError.value = false
        _isLoading.value = false

        val client = token?.let {
            ApiConfig().getApiService().getStories(token = "Bearer $it", size = 100, location = 0)
        }

        client?.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.body()?.error == false) {
                    _listStories.value = response.body()?.listStory
                    _isError.value = false
                    _isLoading.value = false
                } else {
                    _isError.value = true
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }

        })
    }
}