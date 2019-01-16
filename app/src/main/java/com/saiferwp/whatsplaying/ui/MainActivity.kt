package com.saiferwp.whatsplaying.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.saiferwp.whatsplaying.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .replace(
                    R.id.content,
                    MainFragment.newInstance()
                )
                .commit()
    }

    fun openSettings() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in_back,
                R.anim.slide_out_back
            )
            .replace(
                R.id.content,
                SettingsFragment.newInstance()
            )
            .addToBackStack(null)
            .commitAllowingStateLoss()
        supportFragmentManager.executePendingTransactions()
    }
}
