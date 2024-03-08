package com.project.storyappproject.adapter


//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.project.storyappproject.data.model.ListStoryItem
//import com.project.storyappproject.databinding.StoryItemRowBinding
//
//class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>(diffCallback){
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = StoryItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val data = getItem(position)
//        if (data != null) {
//            holder.bind(data)
//        }
//    }
//
//    class ViewHolder(private val binding: StoryItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(data: ListStoryItem) {
//            binding.apply {
//                userName.text = data.name
//                Glide.with(itemView.context)
//                    .load(data.photoUrl)
//                    .into(photo)
//                itemView.setOnClickListener {
////                    intent detail
//                }
//            }
//        }
//    }
//
//    companion object {
//        private val diffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
//            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
//                return oldItem.id == newItem.id
//            }
//        }
//    }
//}