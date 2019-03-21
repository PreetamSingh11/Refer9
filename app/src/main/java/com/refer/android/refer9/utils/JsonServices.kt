package com.refer.android.refer9.utils


import android.content.Context
import java.io.IOException

@Suppress("unused")
object JsonServices {
    fun getJsonFromLocalFile(context: Context, filename: String): String? {
        var json: String? = null
        try {
            val `is` = context.assets.open(filename)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            // close the stream --- very important
            `is`.close()
            // convert byte to string
            json = String(buffer, charset("UTF-8") )
        } catch (ex: IOException) {
            ex.printStackTrace()
            return json
        }

        return json
    }

}
