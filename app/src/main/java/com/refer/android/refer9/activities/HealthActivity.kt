package com.refer.android.refer9.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.refer.android.refer9.R
import com.refer.android.refer9.adapters.ServicesListAdapter
import kotlinx.android.synthetic.main.activity_health.*

class HealthActivity : AppCompatActivity() {

    private val servicesList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health)

        servicesList.add("Doctor")
        servicesList.add("Radiologist")
        servicesList.add("Medical Store")

        rv_health_profiles_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter= ServicesListAdapter(servicesList,::openRegister)
        }
    }
    private fun openRegister(msg:String){
        val intent = Intent(this,RegisterActivity::class.java)
        intent.putExtra("Fragment",msg)
        startActivity(intent)
    }
}
