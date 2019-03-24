package com.refer.android.refer9.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.refer.android.refer9.R
import kotlinx.android.synthetic.main.fragment_chart.view.*

class ChartFragment : Fragment() {

    private lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_chart, container, false)

        getPieChart()
        return rootView
    }
    private fun getPieChart() {
        rootView.fragment_chart.setBackgroundColor(Color.TRANSPARENT)

        moveOffScreen()

        rootView.fragment_chart.setUsePercentValues(true)
        rootView.fragment_chart.description.isEnabled = false

        rootView.fragment_chart.setCenterTextTypeface(Typeface.DEFAULT)
        rootView.fragment_chart.centerText = generateCenterSpannableText()

        rootView.fragment_chart.isDrawHoleEnabled = true
        rootView.fragment_chart.setHoleColor(Color.WHITE)

        rootView.fragment_chart.setTransparentCircleColor(Color.WHITE)
        rootView.fragment_chart.setTransparentCircleAlpha(0)

        rootView.fragment_chart.holeRadius = 58f
        rootView.fragment_chart.transparentCircleRadius = 61f
        rootView.fragment_chart.setHoleColor(Color.TRANSPARENT)

        rootView.fragment_chart.setDrawCenterText(true)

        rootView.fragment_chart.legend.isEnabled = false

        rootView.fragment_chart.isRotationEnabled = false
        rootView.fragment_chart.isHighlightPerTapEnabled = true

        rootView.fragment_chart.maxAngle = 180f // HALF CHART
        rootView.fragment_chart.rotationAngle = 180f
        rootView.fragment_chart.setCenterTextOffset(0F, -20F)

        setData(3, 100f)

        rootView.fragment_chart.animateY(1400, Easing.EaseInOutQuad)

        rootView.fragment_chart.setEntryLabelColor(Color.WHITE)
        rootView.fragment_chart.setEntryLabelTypeface(Typeface.DEFAULT)
        rootView.fragment_chart.setEntryLabelTextSize(16f)
    }

    private fun moveOffScreen() {

        val displayMetrics = DisplayMetrics()
        //windowManager.defaultDisplay.getMetrics(displayMetrics)

        val height = displayMetrics.heightPixels
        val offset = (height * 0.2).toInt() /* percent to move */

        val rlParams = rootView.fragment_chart.layoutParams as RelativeLayout.LayoutParams
        rlParams.setMargins(16, 16, 16, -offset)
        rootView.fragment_chart.layoutParams = rlParams
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
        rootView.fragment_chart.data = data

        rootView.fragment_chart.invalidate()
    }
}
