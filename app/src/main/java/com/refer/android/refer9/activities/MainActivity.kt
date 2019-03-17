package com.refer.android.refer9.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.refer.android.refer9.R
import kotlinx.android.synthetic.main.activity_main.*
import utils.ToastServices

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getLoginStatus()
        setUserName()

        menu_icon.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked=true
            drawerLayout.closeDrawers()
            when(menuItem.itemId){
                R.id.user->{
                    ToastServices.sToast(this,"Profile")
                    login()
                }
                R.id.refer_details->{
                    ToastServices.sToast(this,"Refer Details")
                }
                R.id.about_us->{
                    ToastServices.sToast(this,"About us")
                }
                R.id.t_and_c->{
                    ToastServices.sToast(this,"Terms an Conditions")
                }
                R.id.help->{
                    ToastServices.sToast(this,"Help")
                }
            }
            true
        }
    }

    private fun getLoginStatus() {
        sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val loginStatus = sharedPreferences.getBoolean("LOGIN_STATUS", false)
        val loginSkip = sharedPreferences.getBoolean("LOGIN_SKIP", false)
        if (!loginStatus && !loginSkip) {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }

    private fun setUserName() {
       // val userName = sharedPreferences.getString("USER_NAME", "Anonymous!")
        //greet_text.text = userName
    }

    @Suppress("UNUSED_PARAMETER")
    private fun login() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun openFinance(view: View) {
        val intent = Intent(this, FinanceActivity::class.java)
        startActivity(intent)
    }
    @Suppress("UNUSED_PARAMETER")
    fun openHealth(view: View) {
    }

    @Suppress("UNUSED_PARAMETER")
    fun openOthers(view: View) {
    }

    @Suppress("UNUSED_PARAMETER")
    fun openGrooming(view: View) {
    }
}
