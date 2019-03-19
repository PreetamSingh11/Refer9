package utils

import android.content.Context
import android.content.SharedPreferences

@Suppress("unused")
object MySharedPreferences {

    fun setPref(context: Context, key: String, value: Any?) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        when (value) {
            is String? -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
        editor.apply()
    }

    inline fun <reified T : Any> getPref(context: Context,key: String, defaultValue: T? = null): T? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        return when (T::class) {
            String::class -> sharedPreferences.getString(key, defaultValue as? String) as T?
            Int::class -> sharedPreferences.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> sharedPreferences.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> sharedPreferences.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> sharedPreferences.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun removePref(context: Context,key: String){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}