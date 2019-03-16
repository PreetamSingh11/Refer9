package utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager

object KeyboardServices{
    fun hide(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }
}