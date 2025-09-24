package com.example.andr_dev

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var sbMaxCockroaches: SeekBar
    private lateinit var tvMaxCockroaches: TextView
    private lateinit var sbSpeed: SeekBar
    private lateinit var tvSpeed: TextView
    private lateinit var sbBonusInterval: SeekBar
    private lateinit var tvBonusInterval: TextView
    private lateinit var sbRoundDuration: SeekBar
    private lateinit var tvRoundDuration: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("GameSettings", AppCompatActivity.MODE_PRIVATE)
        sbMaxCockroaches = view.findViewById(R.id.sbMaxCockroaches)
        tvMaxCockroaches = view.findViewById(R.id.tvMaxCockroaches)
        sbSpeed = view.findViewById(R.id.sbSpeed)
        tvSpeed = view.findViewById(R.id.tvSpeed)
        sbBonusInterval = view.findViewById(R.id.sbBonusInterval)
        tvBonusInterval = view.findViewById(R.id.tvBonusInterval)
        sbRoundDuration = view.findViewById(R.id.sbRoundDuration)
        tvRoundDuration = view.findViewById(R.id.tvRoundDuration)

        // Загрузка сохраненных значений или значения по умолчанию
        val defaultMaxBugs = 5
        val defaultSpeed = 3000L
        val defaultBonusInterval = 5000L
        val defaultDuration = 60000L
        sbMaxCockroaches.progress = sharedPreferences.getInt("maxBugs", defaultMaxBugs)
        sbSpeed.progress = ((10000L - sharedPreferences.getLong("bugSpeed", defaultSpeed)) / 1000).toInt()
        sbBonusInterval.progress = ((sharedPreferences.getLong("bonusInterval", defaultBonusInterval) / 1000).toInt() - 1)
        sbRoundDuration.progress = ((sharedPreferences.getLong("gameDuration", defaultDuration) / 10000).toInt() - 6)

        // Обновление текста и сохранение значений при изменении
        sbMaxCockroaches.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvMaxCockroaches.text = "Макс. жуков: $progress"
                sharedPreferences.edit().putInt("maxBugs", progress).apply()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        sbSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val speedDuration = 10000L - (progress * 900L)
                tvSpeed.text = "Скорость: ${progress} сек"
                sharedPreferences.edit().putLong("bugSpeed", speedDuration).apply()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        sbBonusInterval.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val interval = (progress + 1) * 1000L // 1000-10000 мс
                tvBonusInterval.text = "Интервал бонусов: ${interval / 1000} сек"
                sharedPreferences.edit().putLong("bonusInterval", interval).apply()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        sbRoundDuration.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val duration = (progress + 6) * 10000L // 60000-120000 мс
                tvRoundDuration.text = "Длительность: ${duration / 1000} сек"
                sharedPreferences.edit().putLong("gameDuration", duration).apply()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Инициализация текста
        tvMaxCockroaches.text = "Макс. жуков: ${sbMaxCockroaches.progress}"
        tvSpeed.text = "Скорость: ${sbSpeed.progress} сек"
        tvBonusInterval.text = "Интервал бонусов: ${sbBonusInterval.progress + 1} сек"
        tvRoundDuration.text = "Длительность: ${sbRoundDuration.progress + 6} сек"
    }
}