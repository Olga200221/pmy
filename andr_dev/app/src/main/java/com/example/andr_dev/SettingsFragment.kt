package com.example.andr_dev

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sbSpeed = view.findViewById<SeekBar>(R.id.sbSpeed)
        val sbMaxCockroaches = view.findViewById<SeekBar>(R.id.sbMaxCockroaches)
        val sbBonusInterval = view.findViewById<SeekBar>(R.id.sbBonusInterval)
        val sbRoundDuration = view.findViewById<SeekBar>(R.id.sbRoundDuration)

    }
}
