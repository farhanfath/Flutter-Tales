package com.project.storyappproject.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.storyappproject.R
import com.project.storyappproject.databinding.ActivityRegisterBinding
import com.project.storyappproject.ui.customview.CustomAlert

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        registerViewModel = RegisterViewModel()
        binding.registerLayout.registerButton.setOnClickListener {
            register()
        }
        animationHandler()
        binding.registerLayout.buttonToLogin.setOnClickListener {
            finish()
        }
    }

    private fun register() {
        val name = binding.registerLayout.nameEt.text.toString()
        val email = binding.registerLayout.emailEt.text.toString()
        val password = binding.registerLayout.passEt.text.toString()

        when {
            name.isEmpty() -> {
                binding.registerLayout.nameEtLayout.error = getString(R.string.name_notif)
            }
            email.isEmpty() -> {
                binding.registerLayout.emailEtLayout.error = getString(R.string.email_notif)
            }
            password.isEmpty() -> {
                binding.registerLayout.passEtLayout.error = getString(R.string.password_notif)
            }
            else -> {
                registerViewModel.userRegister(name, password, email)

                registerViewModel.isLoading.observe(this) { isLoading ->
                    showLoading(isLoading)
                }

                registerViewModel.isError.observe(this) {
                    errorAlert(it)
                }

                registerViewModel.isSuccess.observe(this) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this, R.string.registerSuccess, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }

                registerViewModel.registerResult.observe(this) { result ->
                    if (result.error) {
                        Toast.makeText(this, result.description, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun errorAlert(isError: Boolean) {
        if (isError) {
            CustomAlert(this, R.string.errorRegister, R.drawable.error_image).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.loadingDescTv.setText(R.string.processRegister)
        if (isLoading) {
            binding.loadingLayout.root.visibility = View.VISIBLE
            binding.registerLayout.root.visibility = View.GONE

        } else {
            binding.loadingLayout.root.visibility = View.GONE
            binding.registerLayout.root.visibility = View.VISIBLE
        }
    }

    private fun animationHandler() {
        binding.apply {

            registerLayout.Titletv.alpha = 0f
            registerLayout.nameEtLayout.alpha = 0f
            registerLayout.emailEtLayout.alpha = 0f
            registerLayout.passEtLayout.alpha = 0f
            registerLayout.registerButton.alpha = 0f
            registerLayout.loginTeksTv.alpha = 0f
            registerLayout.buttonToLogin.alpha = 0f

            val titleAnimator = ObjectAnimator.ofFloat(registerLayout.Titletv, View.ALPHA, 0f, 1f)
            titleAnimator.duration = 700

            val nameLayoutAnimator = ObjectAnimator.ofFloat(registerLayout.nameEtLayout, View.ALPHA, 0f, 1f)
            nameLayoutAnimator.duration = 700

            val emailLayoutAnimator = ObjectAnimator.ofFloat(registerLayout.emailEtLayout, View.ALPHA, 0f, 1f)
            emailLayoutAnimator.duration = 700

            val passLayoutAnimator = ObjectAnimator.ofFloat(registerLayout.passEtLayout, View.ALPHA, 0f, 1f)
            passLayoutAnimator.duration = 700

            val loginButtonAnimator = ObjectAnimator.ofFloat(registerLayout.registerButton, View.ALPHA, 0f, 1f)
            loginButtonAnimator.duration = 700

            val loginTeksAnimator = ObjectAnimator.ofFloat(registerLayout.loginTeksTv, View.ALPHA, 0f, 1f)
            loginTeksAnimator.duration = 700

            val buttonToLoginAnimator = ObjectAnimator.ofFloat(registerLayout.buttonToLogin, View.ALPHA, 0f, 1f)
            buttonToLoginAnimator.duration = 700


            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(
                titleAnimator,
                nameLayoutAnimator,
                emailLayoutAnimator,
                passLayoutAnimator,
                loginButtonAnimator,
                loginTeksAnimator,
                buttonToLoginAnimator
            )

            animatorSet.start()
        }
    }
}