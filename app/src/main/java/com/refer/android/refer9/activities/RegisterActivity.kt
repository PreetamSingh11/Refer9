package com.refer.android.refer9.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.refer.android.refer9.R
import com.refer.android.refer9.fragments.DoctorRegisterFragment
import com.refer.android.refer9.fragments.LabRegisterFragment
import com.refer.android.refer9.utils.ToastServices

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val intent = intent
        val selectedFragmented = intent.getStringExtra("Fragment")
        when (selectedFragmented) {
            "Doctor" -> addDoctorRegisterFragment()
            "Radiologist" -> addLabRegisterFragment()
            "Medical Store" -> addLabRegisterFragment()
            else -> ToastServices.customToastError(this, "Something went wrong")
        }
    }

    private fun addDoctorRegisterFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(com.refer.android.refer9.R.id.fragment_register_container, DoctorRegisterFragment())
        ft.commit()
    }

    private fun addLabRegisterFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(com.refer.android.refer9.R.id.fragment_register_container, LabRegisterFragment.newInstance())
        ft.commit()
    }
}
