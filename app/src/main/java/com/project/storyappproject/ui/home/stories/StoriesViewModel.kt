package com.project.storyappproject.ui.home.stories

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.storyappproject.data.api.ApiConfig
import com.project.storyappproject.data.datapaging.StoryRepository
import com.project.storyappproject.data.datastore.UserPreference
import com.project.storyappproject.data.model.response.ListStoryItem
import com.project.storyappproject.data.model.response.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel(repo: StoryRepository): ViewModel() {
    val getListStory: LiveData<PagingData<ListStoryItem>> =
        repo.getListStories().cachedIn(viewModelScope)
}