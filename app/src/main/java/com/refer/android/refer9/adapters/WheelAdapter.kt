package com.refer.android.refer9.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.refer.android.refer9.R
import github.hellocsl.cursorwheel.CursorWheelLayout
import com.refer.android.refer9.models.WheelData

class WheelAdapter : CursorWheelLayout.CycleWheelAdapter {

    private var mContext: Context? = null
    private var menuIcons: List<WheelData>? = null
    private var inflater: LayoutInflater? = null
    private var gravity: Int = 0

    constructor(mContext: Context, menuIcons: List<WheelData>) {
        this.mContext = mContext
        this.menuIcons = menuIcons
        gravity = Gravity.CENTER
        inflater = LayoutInflater.from(mContext)
    }

    constructor(mContext: Context, menuIcons: List<WheelData>, gravity: Int) {
        this.mContext = mContext
        this.menuIcons = menuIcons
        this.gravity = gravity
        inflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
        return menuIcons!!.size
    }

    @SuppressLint("InflateParams")
    override fun getView(parent: View, position: Int): View {
        val iconsData = getItem(position)
        val root = inflater!!.inflate(R.layout.wheel_icons, null, false)
        val imageView = root.findViewById<ImageView>(R.id.wheel_icon)
        val textView = root.findViewById<TextView>(R.id.wheel_icon_text)
        imageView.setImageResource(iconsData.imageResource)
        textView.text = iconsData.imageDescription
        return root
    }

    override fun getItem(position: Int): WheelData {
        return menuIcons!![position]
    }
}
