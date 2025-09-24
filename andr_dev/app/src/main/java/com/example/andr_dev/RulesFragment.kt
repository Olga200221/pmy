package com.example.andr_dev

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class RulesFragment : Fragment(R.layout.fragment_rules) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnStartGame: Button = view.findViewById(R.id.btnStartGame)
        btnStartGame.setOnClickListener {
            val intent = Intent(requireContext(), GameActivity::class.java)
            startActivity(intent)
        }

        val tvRules: TextView = view.findViewById(R.id.tvRules)
        tvRules.text = getString(R.string.rules_text)
    }
}