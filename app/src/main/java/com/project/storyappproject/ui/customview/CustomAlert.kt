package com.project.storyappproject.ui.customview

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.project.storyappproject.databinding.LayoutAlertBinding

class CustomAlert(context: Context, private var message: Int, private val image: Int): AlertDialog(context) {
    init {
        setCancelable(false)
    }

    private lateinit var binding: LayoutAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.errorMessage.text = context.getString(message)
        binding.alertImage.setImageResource(image)
        binding.okayBtn.setOnClickListener {
            dismiss()
        }
    }
}