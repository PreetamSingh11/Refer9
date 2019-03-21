package com.refer.android.refer9.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.refer.android.refer9.R
import kotlinx.android.synthetic.main.activity_signup.*
import com.refer.android.refer9.utils.KeyboardServices

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signUp_activity_id.setOnClickListener{
            KeyboardServices.hide(this)
        }

        signUp_password.transformationMethod = PasswordTransformationMethod.getInstance()

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        val register = findViewById<Button>(R.id.register)
        register.setOnClickListener {
            register(it)
        }

        val signInForm = findViewById<TextView>(R.id.loginForm)
        signInForm.setOnClickListener {
            login(it)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun login(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun register(view: View) {
        hideKeyboard()
        val nameBox = findViewById<AutoCompleteTextView>(R.id.name)
        val emailBox = findViewById<AutoCompleteTextView>(R.id.email)
        val passwordBox = findViewById<EditText>(R.id.signUp_password)
        val name = nameBox.text.toString()
        val email = emailBox.text.toString().trim { it <= ' ' }
        val password = passwordBox.text.toString().trim { it <= ' ' }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                onSuccess(name)
            }
        }
    }

    private fun onSuccess(name : String) {
        val editor = sharedPreferences.edit()
        editor.putString("USER_NAME", name)
        editor.apply()
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}
