package com.saiferwp.whatsplaying.utils


import android.app.ActivityManager
import android.content.Context
import android.support.v4.content.ContextCompat


class ServiceUtils {

    companion object {
        fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
            val manager = ContextCompat.getSystemService(context, ActivityManager::class.java)
                    ?: return false

            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }
}