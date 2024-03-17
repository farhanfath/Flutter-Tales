package com.project.storyappproject.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.project.storyappproject.R
import com.project.storyappproject.data.datastore.UserPreference
import com.project.storyappproject.data.model.user.UserModel
import com.project.storyappproject.databinding.ActivityStoryBinding
import com.project.storyappproject.databinding.NavHeaderStoryBinding

class StoryActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityStoryBinding
    private lateinit var userPrefModel : UserPreference
    private lateinit var userModel: UserModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefModel = UserPreference(this)
        userModel = userPrefModel.getUser()

        setSupportActionBar(binding.appBarStory.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        setupProfileHeader(binding)

        navController = findNavController(R.id.nav_host_fragment_content_story)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_stories, R.id.nav_profile, R.id.nav_setting
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_story)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupProfileHeader(binding: ActivityStoryBinding) {
        val headerBinding = NavHeaderStoryBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.userName.text = userModel.name
        headerBinding.userId.text = userModel.userId
    }
}