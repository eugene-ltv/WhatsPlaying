package com.saiferwp.whatsplaying.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.saiferwp.whatsplaying.R
import com.saiferwp.whatsplaying.service.VoiceService
import com.saiferwp.whatsplaying.utils.ServiceShutdownIntentFilter
import com.saiferwp.whatsplaying.utils.ServiceUtils

class MainFragment : Fragment() {

    private lateinit var switchOnOff: SwitchCompat
    private lateinit var buttonSettings: ImageButton

    private val serviceShutdownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            switchOnOff.isChecked = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager
            .getInstance(requireContext())
            .registerReceiver(serviceShutdownReceiver, ServiceShutdownIntentFilter())
    }

    override fun onDestroy() {
        LocalBroadcastManager
            .getInstance(requireContext())
            .unregisterReceiver(serviceShutdownReceiver)
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        switchOnOff = view.findViewById(R.id.main_switch_on_off)

        buttonSettings = view.findViewById(R.id.main_button_settings)
        buttonSettings.setOnClickListener {
            (requireActivity() as MainActivity).openSettings()
        }
    }

    override fun onResume() {
        super.onResume()
        val isRunning = ServiceUtils.isServiceRunning(requireContext(), VoiceService::class.java)
        switchOnOff.isChecked = isRunning
        switchOnOff.setOnCheckedChangeListener{
                _,checked ->
            if (checked) {
                requireContext().startService(Intent(requireContext(), VoiceService::class.java))
            } else {
                requireContext().stopService(Intent(requireContext(), VoiceService::class.java))
            }
        }
    }

    companion object {
        fun newInstance() : MainFragment {
            return MainFragment()
        }
    }
}