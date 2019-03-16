package com.refer.android.refer9.activities


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.refer.android.refer9.R
import kotlinx.android.synthetic.main.activity_login.*
import utils.KeyboardServices

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        activity_login_id.setOnClickListener{
            KeyboardServices.hide(this)
        }

        sign_in_button.setOnClickListener{
            login(it)
        }

        signUp.setOnClickListener{
            signUp(it)
        }

        skip.setOnClickListener{
            skip(it)
        }

        login_password.transformationMethod = PasswordTransformationMethod.getInstance()

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun signUp(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun login(view: View) {
        KeyboardServices.hide(this)
        val emailBox: AutoCompleteTextView = findViewById(R.id.email)
        val passwordBox: EditText = findViewById(R.id.signUp_password)
        val email = emailBox.text.toString().trim { it <= ' ' }
        val password = passwordBox.text.toString().trim { it <= ' ' }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this
            ) { task ->
                if (task.isSuccessful) {
                    onSuccessfulLogin()
                } else {
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun onSuccessfulLogin() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("LOGIN_STATUS", true)
        editor.apply()
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun skip(view: View) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("LOGIN_SKIP", true)
        editor.apply()
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}


