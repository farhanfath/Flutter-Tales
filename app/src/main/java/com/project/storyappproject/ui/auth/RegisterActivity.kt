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
import com.project.storyappproject.databinding.ActivityRegisterBinding
import com.project.storyappproject.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        registerViewModel.registerResponse.observe(this) { response ->
            response.message?.let { Log.d(TAG, it) }
            if (response != null) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.getUser.observe(this) { user ->
            this.user = user
        }

        registerViewModel.message.observe(this) {
            if (it.isNotEmpty() && !user.isLogin) {
                showToast(it)
            }
        }

        setupView()
        registerButtonAction()
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

    private fun registerButtonAction() {
        binding.apply {
            registerButton.setOnClickListener {
                val name = nameEt.text.toString()
                val email = emailEt.text.toString()
                val password = passEt.text.toString()
                when {
                    name.isEmpty() -> nameEtLayout.error = getString(R.string.name_notif)
                    email.isEmpty() -> nameEtLayout.error = getString(R.string.email_notif)
                    password.isEmpty() -> nameEtLayout.error = getString(R.string.password_notif)
                    password.length >= 8 -> {
                        registerViewModel.userRegister(name, email, password)
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "Nahraf"
    }
}