package com.refer.android.refer9.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.refer.android.refer9.R
import com.refer.android.refer9.utils.MySharedPreferences
import kotlinx.android.synthetic.main.activity_confirm.*


class ConfirmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        val bundle = intent.extras
        val address = bundle.getString("city") +", "+bundle.getString("state")
        val loan = bundle.getString("loanType") +", "+  bundle.getString("bank")
        text_view_name.text = bundle.getString("name")
        text_view_phone.text = bundle.getString("phone")
        text_view_loan.text = loan
        text_view_address.text = address
        text_view_email.text = bundle.getString("email")
        text_view_occupation.text = bundle.getString("occupation")
        text_view_amount.text = bundle.getString("amount")

        val referer = MySharedPreferences.getPref(this,"USER_NAME", "Anonymous")
        text_view_referer.text = referer

        button_confirm.setOnClickListener{
           val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
