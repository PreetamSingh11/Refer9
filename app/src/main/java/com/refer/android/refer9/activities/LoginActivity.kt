package com.refer.android.refer9.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.refer.android.refer9.fragments.ServicesListFragment
import com.refer.android.refer9.fragments.SignInFragment
import com.refer.android.refer9.fragments.SignUpFragment
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.refer.android.refer9.R.layout.activity_login)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        addSignInFragment()

        back_button_login.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
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

    fun addServicesListFragment(){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(com.refer.android.refer9.R.id.fragment_login_container, ServicesListFragment())
        ft.addToBackStack(SignUpFragment().javaClass.simpleName)
        ft.commit()
    }
}