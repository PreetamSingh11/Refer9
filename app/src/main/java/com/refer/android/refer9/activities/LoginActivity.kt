package com.refer.android.refer9.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.refer.android.refer9.fragments.SignInFragment
import com.refer.android.refer9.fragments.SignUpFragment


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.refer.android.refer9.R.layout.activity_login)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val actionBar = supportActionBar
        actionBar?.title = "Log In"

        addSignInFragment()
    }

    fun addSignInFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(com.refer.android.refer9.R.id.fragment_login_container, SignInFragment())
        ft.commit()
    }

    fun addSignUpFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(com.refer.android.refer9.R.id.fragment_login_container, SignUpFragment())
        ft.addToBackStack(SignInFragment().javaClass.simpleName)
        ft.commit()
    }
}