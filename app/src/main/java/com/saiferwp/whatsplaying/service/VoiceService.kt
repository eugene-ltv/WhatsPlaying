package com.saiferwp.whatsplaying.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import com.saiferwp.whatsplaying.R
import com.saiferwp.whatsplaying.ui.MainActivity
import com.saiferwp.whatsplaying.utils.MetachangedIntentFilter
import com.saiferwp.whatsplaying.utils.Preferences
import com.saiferwp.whatsplaying.utils.ServiceShutdownIntentFilter


class VoiceService : Service(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private lateinit var audioManager: AudioManager
    private lateinit var preferences: Preferences

    private val metaReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (isOnlyHeadsetOptionSet() && !audioManager.isWiredHeadsetOn) {
                return
            }
            announce(intent)
        }
    }

    private fun announce(intent: Intent) {
        tts?.stop()
        var artist = intent.getStringExtra("artist") ?: ""
        var track = intent.getStringExtra("track") ?: ""
        if (artist.isNotBlank()) {
            tts?.speak(artist, TextToSpeech.QUEUE_ADD, null)
        } else {
            artist = "?"
        }
        if (track.isNotBlank()) {
            tts?.speak(track, TextToSpeech.QUEUE_ADD, null)
        } else {
            track = "?"
        }

        Toast.makeText(this, getString(R.string.toast_currently_playing, artist, track), Toast.LENGTH_LONG).show()
    }

    private fun isOnlyHeadsetOptionSet(): Boolean {
        return preferences.getOutputPref() == Preferences.OutputPref.ONLY_HEADPHONES
    }

    override fun onCreate() {
        super.onCreate()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        preferences = Preferences.getInstance(this)

        val filter = MetachangedIntentFilter()
        registerReceiver(metaReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()

        startForeground(1, notification)

        tts?.shutdown()
        tts = TextToSpeech(this, this)

        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        tts?.shutdown()
        unregisterReceiver(metaReceiver)
        LocalBroadcastManager
            .getInstance(this)
            .sendBroadcast(ServiceShutdownIntentFilter.getIntent())
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.ERROR) {
            Toast.makeText(
                this,
                R.string.toast_tts_error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun createNotification(): Notification {
        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                this.javaClass.simpleName,
                getString(R.string.notification_channel),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.setSound(null, null)
            notificationChannel.enableVibration(false)
            notificationChannel.enableLights(false)

            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(notificationChannel)

            NotificationCompat.Builder(this, notificationChannel.id)
        } else {
            NotificationCompat.Builder(this)
        }

        val openIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, openIntent, 0)

        val shutdownIntent = Intent(this, ShutdownReceiver::class.java)
        val shutdownPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, shutdownIntent, 0)

        notificationBuilder
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(resources.getColor(R.color.colorPrimary))
            .setContentIntent(openPendingIntent)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_subtitle))
            .setOngoing(true)
            .addAction(0, getString(R.string.notification_turn_off), shutdownPendingIntent)
            .setAutoCancel(false)
            .setVibrate(null)
            .setSound(null, 0)
            .setLights(0, 0, 0)
        return notificationBuilder.build()
    }
}