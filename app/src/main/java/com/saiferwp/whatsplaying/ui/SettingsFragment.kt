package com.saiferwp.whatsplaying.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import com.saiferwp.whatsplaying.R
import com.saiferwp.whatsplaying.utils.Preferences


class SettingsFragment : Fragment() {

    private lateinit var titleSettings: TextView
    private lateinit var buttonTTS: Button
    private lateinit var outputRadioGroup: RadioGroup

    private lateinit var preferences: Preferences

    private val outputPrefsTypeMap = hashMapOf(
        R.id.settings_output_option_headphones to Preferences.OutputPref.ONLY_HEADPHONES,
        R.id.settings_output_option_all to Preferences.OutputPref.ALL
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferences = Preferences.getInstance(requireContext())

        titleSettings = view.findViewById(R.id.settings_title)
        titleSettings.setOnClickListener {
            requireActivity().onBackPressed()
        }

        buttonTTS = view.findViewById(R.id.settings_button_tts)
        buttonTTS.setOnClickListener {
            val intent = Intent()
            intent.action = "com.android.settings.TTS_SETTINGS"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this.startActivity(intent)
        }

        val outputPref = preferences.getOutputPref()
        outputRadioGroup = view.findViewById(R.id.settings_output_group)
        outputRadioGroup.check(
            outputPrefsTypeMap.entries.find { outputPref == it.value }?.key ?: R.id.settings_output_option_headphones
        )
        outputRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val checked = outputPrefsTypeMap[checkedId]
            checked?.let {
                Preferences.getInstance(requireContext()).setOutputPref(it)
            }
        }
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}