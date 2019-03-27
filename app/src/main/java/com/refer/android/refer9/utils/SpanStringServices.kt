package com.refer.android.refer9.utils

import android.app.Activity
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.refer.android.refer9.activities.LoginActivity

object SpanStringServices {
    fun createSpannableString(activity: Activity,mTextView: TextView,string: String,start: Int,end: Int,flag:Int){
        val ss = SpannableString(string)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                if (flag ==1){
                    (activity as LoginActivity).addSignInFragment()
                } else {
                    (activity as LoginActivity).addSignUpFragment()
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mTextView.text = ss
        mTextView.movementMethod = LinkMovementMethod.getInstance()
        mTextView.highlightColor= Color.GREEN
    }
}