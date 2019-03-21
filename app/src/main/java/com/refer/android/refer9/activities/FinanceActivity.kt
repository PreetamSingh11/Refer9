package com.refer.android.refer9.activities

import com.refer.android.refer9.adapters.WheelAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.refer.android.refer9.R
import github.hellocsl.cursorwheel.CursorWheelLayout
import kotlinx.android.synthetic.main.activity_finance.*
import com.refer.android.refer9.models.WheelData
import com.refer.android.refer9.utils.ToastServices
import java.util.*

class FinanceActivity : AppCompatActivity(), CursorWheelLayout.OnMenuSelectedListener {

    private lateinit var wheelIcons: CursorWheelLayout
    private lateinit var wIcons: MutableList<WheelData>
    private lateinit var desc : TextView
    private lateinit var heading : TextView
    private lateinit var backgroundIcon : ImageView

    private var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finance)

        desc = findViewById(R.id.description)
        heading = findViewById(R.id.descriptionHeading)
        backgroundIcon = findViewById(R.id.textureImage)

        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener{
            openReferDetails(it)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            desc.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
        }

        initViews()
        loadData()

        wheelIcons.setOnMenuSelectedListener(this)
    }

    private fun initViews() {
        wheelIcons = findViewById<View>(R.id.wheel_image) as CursorWheelLayout
    }

    private fun loadData() {
        wIcons = ArrayList()
        wIcons.add(WheelData(R.drawable.loan, "Loan"))
        wIcons.add(WheelData(R.drawable.insurance, "Insurance"))
        wIcons.add(WheelData(R.drawable.mutual_fund, "Mutual Fund"))
        wIcons.add(WheelData(R.drawable.fixed_deposit, "Fixed Deposit"))
        wIcons.add(WheelData(R.drawable.equity, "Equity"))
        wIcons.add(WheelData(R.drawable.loan, "Loan"))
        wIcons.add(WheelData(R.drawable.insurance, "Insurance"))
        wIcons.add(WheelData(R.drawable.mutual_fund, "Mutual Fund"))

        val wheelIconsAdapter = WheelAdapter(baseContext, wIcons)
        wheelIcons.setAdapter(wheelIconsAdapter)
    }


    override fun onItemSelected(parent: CursorWheelLayout, view: View, pos: Int) {
        if (parent.id == R.id.wheel_image) {
            setLayout(wIcons[pos].imageDescription)
        } else {
            ToastServices.sToast(this,"Something is wrong in condition ")
        }
    }

    private fun setLayout(imageDescription: String) {
        desc.text = " "
        heading.text = imageDescription
        nextButton.isEnabled=false
        nextButton.text=resources.getText(R.string.coming_soon_text)
        when (imageDescription) {
            "Loan" -> {
                description = resources.getString(R.string.loan_description)
                backgroundIcon.setImageResource(R.drawable.loan)
                nextButton.isEnabled=true
                nextButton.text=resources.getText(R.string.next_button_text)
            }
            "Mutual Fund" -> {
                description = resources.getString(R.string.mutualFund_description)
                backgroundIcon.setImageResource(R.drawable.mutual_fund)
            }
            "Insurance" -> {
                description = resources.getString(R.string.insurance_description)
                backgroundIcon.setImageResource(R.drawable.insurance)
            }
            "Fixed Deposit" -> {
                description = resources.getString(R.string.fixedDeposit_description)
                backgroundIcon.setImageResource(R.drawable.fixed_deposit)
            }
            "Equity" -> {
                description = resources.getString(R.string.equity_description)
                backgroundIcon.setImageResource(R.drawable.equity)
            }
            else -> {
                ToastServices.sToast(this, imageDescription)
            }
        }
        desc.text = description
    }

    @Suppress("UNUSED_PARAMETER")
    fun openReferDetails(view: View){
        val intent = Intent(this, ReferDetailActivity::class.java)
        startActivity(intent)
    }
}
