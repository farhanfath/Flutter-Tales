package com.project.storyappproject.customview

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputEditText(context, attrs, defStyleAttr), TextWatcher {

    init {
        addTextChangedListener(this)
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        validatePassword(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {}

    private fun validatePassword(password: String) {
        val isValid = password.length >= 8 && password.toHashSet().size == password.length
        error = if (isValid) null else "Password harus berisi minimal 8 karakter unik"
    }
}