package com.refer.android.refer9.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.firebase.auth.FirebaseAuth
import com.refer.android.refer9.R
import com.refer.android.refer9.utils.MySharedPreferences
import com.refer.android.refer9.utils.ToastServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_header.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(com.refer.android.refer9.R.layout.activity_main)

        getLoginStatus()
        setUserProfile()

        getPieChart()

        navigationView.getHeaderView(0).userProfile.setOnClickListener {
            login()
        }

        menu_icon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                com.refer.android.refer9.R.id.refer_details -> {
                    ToastServices.sToast(this, "Refer Details")
                }
                com.refer.android.refer9.R.id.about_us -> {
                    ToastServices.sToast(this, "About us")
                }
                com.refer.android.refer9.R.id.t_and_c -> {
                    ToastServices.sToast(this, "Terms an Conditions")
                }
                com.refer.android.refer9.R.id.help -> {
                    ToastServices.sToast(this, "Help")
                }
                com.refer.android.refer9.R.id.signOut -> {
                    FirebaseAuth.getInstance().signOut()
                    MySharedPreferences.setPref(this, "LOGIN_STATUS", false)
                    setUserProfile()
                }
            }
            true
        }
    }

    private fun getPieChart() {
        chart.setBackgroundColor(Color.TRANSPARENT)

        moveOffScreen()

        chart.setUsePercentValues(true)
        chart.description.isEnabled = false

        chart.setCenterTextTypeface(Typeface.DEFAULT)
        chart.centerText = generateCenterSpannableText()

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)

        chart.legend.isEnabled = false

        chart.isRotationEnabled = false
        chart.isHighlightPerTapEnabled = true

        chart.maxAngle = 180f // HALF CHART
        chart.rotationAngle = 180f
        chart.setCenterTextOffset(0F, -20F)

        setData(3, 100f)

        chart.animateY(1400, Easing.EaseInOutQuad)

        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTypeface(Typeface.DEFAULT)
        chart.setEntryLabelTextSize(16f)
    }

    private fun moveOffScreen() {

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val height = displayMetrics.heightPixels
        val offset = (height * 0.2).toInt() /* percent to move */

        val rlParams = chart.layoutParams as RelativeLayout.LayoutParams
        rlParams.setMargins(16, 16, 16, -offset)
        chart.layoutParams = rlParams
    }

    private fun generateCenterSpannableText(): SpannableString {
        val s = SpannableString("Reward Points: 140")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 18, 0)
        return s
    }

    private fun setData(count: Int, range: Float) {

        val values = ArrayList<PieEntry>()

        for (i in 0 until count) {
            values.add(PieEntry((Math.random() * range + range / 5).toFloat()))
        }

        val dataSet = PieDataSet(values, "User Data")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        dataSet.setColors(Color.parseColor("#00ff00"), Color.parseColor("#FFBF00"), Color.parseColor("#ff0000"))

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(Typeface.DEFAULT)
        chart.data = data

        chart.invalidate()
    }

    private fun getLoginStatus() {
        val loginStatus = MySharedPreferences.getPref(this,"LOGIN_STATUS", false)
        val loginSkip = MySharedPreferences.getPref(this,"LOGIN_SKIP", false)
        if (!loginStatus!! && !loginSkip!!) {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }

    private fun setUserProfile() {
        if (MySharedPreferences.getPref(this, "LOGIN_STATUS", false)!!) {
            val userNameValue = MySharedPreferences.getPref(this, "USER_NAME", "Anonymous!")
            navigationView.getHeaderView(0).userName.text = userNameValue
            navigationView.menu.findItem(R.id.signOut).isEnabled = true
            navigationView.menu.findItem(R.id.refer_details).isEnabled = true
        } else{
            navigationView.getHeaderView(0).userName.text = resources.getString(R.string.anonymous)
            navigationView.menu.findItem(R.id.signOut).isEnabled = false
            navigationView.menu.findItem(R.id.refer_details).isEnabled = false
        }
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
