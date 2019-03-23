package com.refer.android.refer9.utils

import android.content.Context
import android.widget.Toast
import com.valdesekamdem.library.mdtoast.MDToast

@Suppress("unused")
object ToastServices {

    fun sToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun lToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun customToastInfo(context: Context, msg: String){
        MDToast.makeText(context, msg, MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show()
    }
    fun customToastSuccess(context: Context, msg: String){
        MDToast.makeText(context, msg, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show()
    }
    fun customToastWarning(context: Context, msg: String){
        MDToast.makeText(context, msg, MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show()
    }
    fun customToastError(context: Context, msg: String){
        MDToast.makeText(context, msg, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show()
    }
}
