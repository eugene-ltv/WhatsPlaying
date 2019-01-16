package com.saiferwp.whatsplaying.utils

import android.content.Context
import android.content.SharedPreferences
import com.saiferwp.whatsplaying.BuildConfig

class Preferences(context: Context) {

    enum class OutputPref {
        ONLY_HEADPHONES,
        ALL
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    fun getOutputPref(): OutputPref {
        val ordinal = sharedPreferences.getInt(OUTPUT_PREF, OutputPref.ONLY_HEADPHONES.ordinal)
        return OutputPref.values()[ordinal]
    }

    fun setOutputPref(outputPref: OutputPref) {
        sharedPreferences.edit().putInt(OUTPUT_PREF, outputPref.ordinal).apply()
    }

    companion object {
        const val OUTPUT_PREF = "output_pref"

        private var instance: Preferences? = null
        fun getInstance(context: Context): Preferences {
            instance = instance ?: Preferences(context)
            return instance as Preferences
        }
    }
}