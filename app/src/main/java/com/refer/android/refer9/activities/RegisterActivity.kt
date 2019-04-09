package com.refer.android.refer9.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.refer.android.refer9.R
import com.refer.android.refer9.fragments.DoctorRegisterFragment

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        addDoctorRegisterFragment()
    }

    private fun addDoctorRegisterFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(com.refer.android.refer9.R.id.fragment_register_container, DoctorRegisterFragment() )
        ft.commit()
    }
}
