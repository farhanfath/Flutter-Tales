package com.project.storyappproject.ui.home.stories

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.storyappproject.adapter.StoryAdapter
import com.project.storyappproject.data.model.response.ListStoryItem
import com.project.storyappproject.databinding.FragmentStoriesBinding
import com.project.storyappproject.ui.home.DetailStoryActivity
import com.project.storyappproject.ui.home.DetailStoryActivity.Companion.DETAIL_STORY
import com.project.storyappproject.ui.home.post.PostActivity

class StoriesFragment : Fragment() {

    private var _binding: FragmentStoriesBinding? = null
    private val binding get() = _binding!!
    private val storiesViewModel: StoriesViewModel by activityViewModels()
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        storiesViewModel.listStories.observe(viewLifecycleOwner) {
            val isEmptyUser = it.isEmpty()
            if (isEmptyUser) {
                Toast.makeText(activity, "Unknown User", Toast.LENGTH_SHORT).show()
            } else {
                showListStories(root.context, it)
            }
        }
        storiesViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        storiesViewModel.isError.observe(viewLifecycleOwner) {
            Log.d("test", "gagal memuat story")
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postButtonHandler()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarStories.visibility = View.VISIBLE
            binding.rvStories.visibility = View.GONE
        } else{
            binding.progressBarStories.visibility = View.GONE
            binding.rvStories.visibility = View.VISIBLE
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

    private fun showListStories(context: Context, stories: List<ListStoryItem>) {
        val storiesRv = binding.rvStories

        isLoading = true
        binding.progressBarStories.visibility = View.VISIBLE
        binding.rvStories.visibility = View.GONE

        val listStoriesAdapter = StoryAdapter(stories)
        storiesRv.adapter = listStoriesAdapter
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            storiesRv.layoutManager = GridLayoutManager(context, 2)
        } else {
            storiesRv.layoutManager = LinearLayoutManager(context)
        }

        listStoriesAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {

            override fun onItemClicked(
                data: ListStoryItem,
                sharedViews: Array<Pair<View, String>>
            ) {
                openDetailStory(data, sharedViews)
            }
        })

        isLoading = false
        binding.progressBarStories.visibility = View.GONE
        binding.rvStories.visibility = View.VISIBLE
    }

    private fun postButtonHandler() {
        binding.createStoryButton.setOnClickListener {
            val intent = Intent(binding.root.context, PostActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}