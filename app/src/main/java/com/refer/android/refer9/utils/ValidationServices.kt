package com.refer.android.refer9.utils

import android.content.Context
import android.util.Patterns
import android.text.TextUtils
import com.refer.android.refer9.R
import com.refer.android.refer9.models.ErrorData
import java.util.regex.Pattern

@Suppress("unused")
object ValidationServices {

    fun isEmailValidFunc(context: Context, email: CharSequence): ErrorData {
        return when {
            TextUtils.isEmpty(email) -> ErrorData(101, context.resources.getString(R.string.empty_value))
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ErrorData(102,context.resources.getString(R.string.error_invalid_email))
            else -> ErrorData(100,"success")
        }
    }

    fun isNameValidFunc(context: Context, name: CharSequence): ErrorData {
        val ps = Pattern.compile("^[a-zA-Z ]+$")
        val ms = ps.matcher(name)
        val bs = ms.matches()
        return when {
            TextUtils.isEmpty(name) -> ErrorData(101, context.resources.getString(R.string.empty_value))
            !bs -> ErrorData(102,context.resources.getString(R.string.error_invalid_name))
            else -> ErrorData(100,"success")
        }
    }

    fun isPasswordValidFunc(context: Context, password: String) : ErrorData {
        return when {
            TextUtils.isEmpty(password) -> ErrorData(101, context.resources.getString(R.string.empty_value))
            (password.length<6) -> ErrorData(102,context.resources.getString(R.string.error_invalid_password))
            else -> ErrorData(100,"success")
        }
    }
}