package com.project.storyappproject.ui.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.storyappproject.R
import com.project.storyappproject.data.model.User
import com.project.storyappproject.databinding.ActivityLoginBinding
import com.project.storyappproject.ui.main.MainActivity
import com.project.storyappproject.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }

    private lateinit var user: User
    private var statusCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        saveUserData()
        loginViewModel.getUser.observe(this) { user ->
            this.user = user
            if (user.isLogin && statusCount == 0) {
                statusCount++
                Log.d(TAG, user.name)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.message.observe(this) {
            if (it.isNotEmpty() && !user.isLogin) {
                showToast(it)
            }
        }

        setupView()
        loginButtonAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun saveUserData() {
        loginViewModel.loginResponse.observe(this) {response ->
            if (response != null) {
                val result = response.loginResult
                user = User(result.name, result.token, true)
                loginViewModel.saveUserPref(user)
            }
        }
    }

    private fun loginButtonAction() {
        binding.apply {
            loginButton.setOnClickListener {
                val email = emailEt.text.toString()
                val password = passEt.text.toString()

                when {
                    email.isEmpty() -> emailEtLayout.error = getString(R.string.email_notif)
                    password.isEmpty() || password.length <= 8 -> passEtLayout.error = getString(R.string.password_notif)

                    else -> {
                        loginViewModel  .userLogin(email, password)
                    }
                }
            }

            buttonToRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "Nahraf"
    }
}





