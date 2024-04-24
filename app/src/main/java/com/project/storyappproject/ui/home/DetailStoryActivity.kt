package com.project.storyappproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.project.storyappproject.R
import com.project.storyappproject.data.model.response.ListStoryItem
import com.project.storyappproject.databinding.ActivityDetailStoryBinding
import com.project.storyappproject.utility.dateFormat


@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val storyDetail = intent.getParcelableExtra<ListStoryItem>(DETAIL_STORY) as ListStoryItem

        setupView(storyDetail)
    }

    private fun setupView(storyDetail: ListStoryItem) {
        Glide.with(this@DetailStoryActivity)
            .load(storyDetail.photoUrl)
            .centerCrop()
            .into(binding.storyDetailIv)

        storyDetail.apply {
            binding.nameDetailTv.text = name
            binding.descDetailTv.text = description
            binding.detailDateTv.text = createdAt.dateFormat()
        }
        binding.apply {
            showBtn.setOnClickListener {
                onAddButtonClicked()
            }
            backHome.setOnClickListener {
                onBackPressed()
            }
            shareBtn.setOnClickListener {
                shareStory()
            }
        }
    }

    private fun shareStory() {
        val shareDetail = intent.getParcelableExtra<ListStoryItem>(DETAIL_STORY)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, ("Story From ${shareDetail?.name}"))
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Send To"))
    }

    private fun onAddButtonClicked() {
        fabSetVisibility(clicked)
        fabSetAnimation(clicked)
        clicked = !clicked
    }

    private fun fabSetVisibility(clicked: Boolean) {
        binding.apply {
            if(!clicked){
                backHome.visibility = View.VISIBLE
                shareBtn.visibility = View.VISIBLE
            }else{
                backHome.visibility = View.INVISIBLE
                shareBtn.visibility = View.INVISIBLE
            }
        }
    }

    private fun fabSetAnimation(clicked: Boolean) {

        binding.apply {
            if(!clicked){
                backHome.startAnimation(fromBottom)
                shareBtn.startAnimation(fromBottom)
                showBtn.startAnimation(rotateOpen)
            }else{
                backHome.startAnimation(toBottom)
                shareBtn.startAnimation(toBottom)
                showBtn.startAnimation(rotateClose)
            }
        }
    }

    companion object {
        const val DETAIL_STORY = "DETAIL_STORY"
    }
}