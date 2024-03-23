package com.project.storyappproject.ui.home.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.storyappproject.data.datapaging.StoryRepository
import com.project.storyappproject.data.model.response.ListStoryItem

class StoriesViewModel(repo: StoryRepository): ViewModel() {
    val getListStory: LiveData<PagingData<ListStoryItem>> =
        repo.getListStories().cachedIn(viewModelScope)
}