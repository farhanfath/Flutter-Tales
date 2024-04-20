package com.project.storyappproject.ui.home.stories

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.storyappproject.adapter.LoadingStateAdapter
import com.project.storyappproject.adapter.StoryAdapter
import com.project.storyappproject.data.model.response.ListStoryItem
import com.project.storyappproject.databinding.FragmentStoriesBinding
import com.project.storyappproject.ui.home.DetailStoryActivity
import com.project.storyappproject.ui.home.DetailStoryActivity.Companion.DETAIL_STORY
import com.project.storyappproject.ui.home.post.PostActivity
import com.project.storyappproject.utility.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

class StoriesFragment : Fragment() {

    private var _binding: FragmentStoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ViewModelFactory
    private val storiesViewModel: StoriesViewModel by viewModels { factory }

    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModelHandler()
        postButtonHandler()
        refreshHandler()

        showListStories()

        return root
    }

    private fun viewModelHandler() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showListStories()
    }

    private fun showListStories() {
        context?.let { showListStories(it) }
        lifecycleScope.launch {
            delay(PostActivity.SPACE_TIME)
            refresh()
        }
    }

    private fun refresh() {
        binding.swipeRefresh.isRefreshing = true
        storyAdapter.refresh()
        Timer().schedule(1000) {
            binding.swipeRefresh.isRefreshing = false
            binding.rvStories.smoothScrollToPosition(0)
        }
    }

    private fun refreshHandler() {
        binding.swipeRefresh.setOnRefreshListener {
            refresh()
        }
    }

    private fun openDetailStory(story: ListStoryItem, sharedViews: Array<Pair<View, String>>) {
        val intent = Intent(binding.root.context, DetailStoryActivity::class.java)
        intent.putExtra(DETAIL_STORY, story)

        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            *sharedViews
        )

        startActivity(intent, optionsCompat.toBundle())
    }

    private fun showListStories(context: Context) {
        storyAdapter = StoryAdapter()
        val storiesRv = binding.rvStories

        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            storiesRv.layoutManager = GridLayoutManager(context, 2)
        } else {
            storiesRv.layoutManager = LinearLayoutManager(context)
        }

        storiesRv.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        storiesViewModel.getListStory.observe(viewLifecycleOwner) {
            storyAdapter.submitData(lifecycle, it)
        }

        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {

            override fun onItemClicked(
                data: ListStoryItem,
                sharedViews: Array<Pair<View, String>>
            ) {
                openDetailStory(data, sharedViews)
            }
        })
    }

    private val postActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            lifecycleScope.launch {
                delay(PostActivity.SPACE_TIME)
                refresh()
            }
        }
    }

    private fun postButtonHandler() {
        binding.createStoryButton.setOnClickListener {
            val intent = Intent(binding.root.context, PostActivity::class.java)
            postActivityResultLauncher.launch(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}