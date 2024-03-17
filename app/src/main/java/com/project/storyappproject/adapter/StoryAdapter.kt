package com.project.storyappproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.storyappproject.data.model.response.ListStoryItem
import com.project.storyappproject.databinding.StoryItemRowBinding
import com.project.storyappproject.dateFormat

class StoryAdapter(
    private val listStories: List<ListStoryItem>
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StoryItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listStories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStories[position], onItemClickCallback)
    }

    class ViewHolder(private val binding: StoryItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem, clickCallback: OnItemClickCallback) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(photo)

                storyName.text = story.name
                storyDesc.text = story.description
                storyDate.text = story.createdAt.dateFormat()
                storyCv.setOnClickListener {
                    clickCallback.onItemClicked(story, arrayOf(
                        Pair(photo, "sharedPhoto"),
                        Pair(storyName, "sharedName"),
                        Pair(storyDesc, "sharedDesc"),
                        Pair(storyDate, "sharedDate")
                    ))
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem, sharedViews: Array<Pair<View, String>>)
    }

}