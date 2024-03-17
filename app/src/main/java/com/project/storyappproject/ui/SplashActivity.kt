package com.project.storyappproject.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.project.storyappproject.R
import com.project.storyappproject.data.datastore.UserPreference
import com.project.storyappproject.data.model.user.UserModel
import com.project.storyappproject.ui.auth.LoginActivity
import com.project.storyappproject.ui.home.StoryActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference
    private lateinit var userModel: UserModel
    private val splashTimer = 2000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userPreference = UserPreference(this)
        userModel =userPreference.getUser()
        intentHandler()
    }

    private fun intentHandler() {
        if (userModel.name != null && userModel.userId != null && userModel.token != null) {
            val intent = Intent(this, StoryActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, splashTimer)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, splashTimer)
        }
    }
}