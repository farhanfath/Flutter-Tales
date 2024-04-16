package com.project.storyappproject.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.storyappproject.R
import com.project.storyappproject.data.datastore.UserPreference
import com.project.storyappproject.data.model.response.AuthResponse
import com.project.storyappproject.data.model.user.UserModel
import com.project.storyappproject.databinding.ActivityLoginBinding
import com.project.storyappproject.ui.customview.CustomAlert
import com.project.storyappproject.ui.home.StoryActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        buttonActionSetup()
        animationHandler()
    }

    private fun buttonActionSetup() {
        binding.apply {
            loginLayout.loginButton.setOnClickListener {
                login()
            }
            loginLayout.buttonToRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun login() {
        val email = binding.loginLayout.emailEt.text.toString()
        val password = binding.loginLayout.passEt.text.toString()

        when {
            email.isEmpty() -> {
                binding.loginLayout.emailEtLayout.error = getString(R.string.email_notif)
            }
            password.isEmpty() -> {
                binding.loginLayout.passEtLayout.error = getString(R.string.password_notif)
            }
            else -> {
                loginViewModel.userLogin(email, password)

                loginViewModel.isError.observe(this) {
                    errorAlert(it)
                }

                loginViewModel.isLoading.observe(this) {
                    showLoading(it)
                }

                loginViewModel.loginResult.observe(this) {
                    loginHandler(it)
                    Toast.makeText(this, R.string.loginSuccess, Toast.LENGTH_SHORT).show()
                }

                loginViewModel.isSuccess.observe(this) {
                    Log.d("login", "Berhasil Login")
                }
            }
        }
    }

    private fun errorAlert(isError: Boolean) {
        if (isError) {
            CustomAlert(this, R.string.errorLogin, R.drawable.error_image).show()
        }
    }

    private fun loginHandler(loginUserData: AuthResponse) {
        if (!loginUserData.error) {
            saveUserData(loginUserData)

            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveUserData(loginUserData: AuthResponse) {
        val loginPreference = UserPreference(this)
        val loginResult = loginUserData.loginResult
        val userModel = UserModel(
            name = loginResult.name, userId = loginResult.userId, token = loginResult.token
        )
        loginPreference.setLogin(userModel)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.loadingDescTv.setText(R.string.processLogin)
        if (isLoading) {
            binding.loadingLayout.root.visibility = View.VISIBLE
            binding.loginLayout.root.visibility = View.GONE

        } else {
            binding.loadingLayout.root.visibility = View.GONE
            binding.loginLayout.root.visibility = View.VISIBLE
        }
    }

    private fun animationHandler() {
        binding.apply {

            loginLayout.Titletv.alpha = 0f
            loginLayout.emailEtLayout.alpha = 0f
            loginLayout.passEtLayout.alpha = 0f
            loginLayout.loginButton.alpha = 0f
            loginLayout.regsiterTeksTv.alpha = 0f
            loginLayout.buttonToRegister.alpha = 0f

            val titleAnimator = ObjectAnimator.ofFloat(loginLayout.Titletv, View.ALPHA, 0f, 1f)
            titleAnimator.duration = 700

            val emailLayoutAnimator = ObjectAnimator.ofFloat(loginLayout.emailEtLayout, View.ALPHA, 0f, 1f)
            emailLayoutAnimator.duration = 700

            val passLayoutAnimator = ObjectAnimator.ofFloat(loginLayout.passEtLayout, View.ALPHA, 0f, 1f)
            passLayoutAnimator.duration = 700

            val loginButtonAnimator = ObjectAnimator.ofFloat(loginLayout.loginButton, View.ALPHA, 0f, 1f)
            loginButtonAnimator.duration = 700

            val registerTextAnimator = ObjectAnimator.ofFloat(loginLayout.regsiterTeksTv, View.ALPHA, 0f, 1f)
            registerTextAnimator.duration = 700

            val registerButtonAnimator = ObjectAnimator.ofFloat(loginLayout.buttonToRegister, View.ALPHA, 0f, 1f)
            registerButtonAnimator.duration = 700

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(
                titleAnimator,
                emailLayoutAnimator,
                passLayoutAnimator,
                loginButtonAnimator,
                registerTextAnimator,
                registerButtonAnimator
            )

            animatorSet.start()
        }
    }
}





