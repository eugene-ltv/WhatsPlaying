package com.saiferwp.whatsplaying.utils

import android.content.IntentFilter

class MetachangedIntentFilter:IntentFilter() {
    init {
        //Default, AIMP
        addAction("com.android.music.metachanged")
        //HTC Music
        addAction("com.htc.music.metachanged")
        //MIUI Player
        addAction("com.miui.player.metachanged")
        //Real
        addAction("com.real.IMP.metachanged")
        //SEMC Music Player
        addAction("com.sonyericsson.music.metachanged")
        //Samsung Music Player
        addAction("com.samsung.sec.android.MusicPlayer.metachanged")
        addAction("com.sec.android.app.music.metachanged")
        //Winamp
        addAction("com.nullsoft.winamp.metachanged")
        //Amazon
        addAction("com.amazon.mp3.metachanged")
        //PowerAmp
        addAction("com.maxmpz.audioplayer.playstatechanged")
        //Last.fm
        addAction("fm.last.android.metachanged")
        //Apollo
        addAction("com.andrew.apollo.metachanged")
    }
}