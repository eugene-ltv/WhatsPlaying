package com.saiferwp.whatsplaying.utils

import android.content.Intent
import android.content.IntentFilter

class ServiceShutdownIntentFilter : IntentFilter(EVENT_NAME) {
    companion object {
        fun getIntent(): Intent {
            return Intent(EVENT_NAME)
        }

        const val EVENT_NAME = "service_shutdown"
    }
}