package com.refer.android.refer9.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.refer.android.refer9.R
import com.refer.android.refer9.fragments.DoctorRegisterFragment
import com.refer.android.refer9.fragments.LabRegisterFragment
import com.refer.android.refer9.utils.MySharedPreferences
import com.refer.android.refer9.utils.ToastServices
import com.refer.android.refer9.viewModels.HealthRegisterModel

class HealthRegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: HealthRegisterModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel = ViewModelProviders.of(this).get(HealthRegisterModel::class.java)

        if (MySharedPreferences.getPref(this, "LOGIN_TYPE_EMAIL", false)!! &&
            MySharedPreferences.getPref(this, "LOGIN_STATUS", false)!!
        ) {
            val intent = intent
            when (intent.getStringExtra("Fragment")) {
                "Doctor" -> addDoctorRegisterFragment("Doctor")
                "Radiologist" -> addLabRegisterFragment("Radiologist")
                "Medical Store" -> addLabRegisterFragment("Medical Store")
                else -> ToastServices.customToastError(this, "Something went wrong")
            }
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun addDoctorRegisterFragment(title:String) {
        viewModel.setFragmentTitle(title)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_register_container, DoctorRegisterFragment())
        ft.commit()
    }

    private fun addLabRegisterFragment(title: String) {
        viewModel.setFragmentTitle(title)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_register_container, LabRegisterFragment.newInstance())
        ft.commit()
    }
}
