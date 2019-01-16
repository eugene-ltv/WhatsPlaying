package com.saiferwp.whatsplaying.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ShutdownReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.stopService(Intent(context, VoiceService::class.java))
    }
}