package com.example.andr_dev

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var gameField: FrameLayout
    private lateinit var tvScore: TextView

    private var score = 0
    private val handler = Handler(Looper.getMainLooper())
    private val bugs = mutableListOf<BugView>()

    // Настройки (можно связать с SettingsFragment)
    private var spawnInterval = 1500L
    private var maxBugs = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameField = findViewById(R.id.gameField)
        tvScore = findViewById(R.id.tvScore)

        startGame()
    }

    private fun startGame() {
        handler.post(spawnRunnable)
    }

    private val spawnRunnable = object : Runnable {
        override fun run() {
            if (bugs.size < maxBugs) {
                spawnBug()
            }
            handler.postDelayed(this, spawnInterval)
        }
    }

    private fun spawnBug() {
        val bug = BugView(this)
        val size = 150
        val params = FrameLayout.LayoutParams(size, size)

        params.leftMargin = Random.nextInt(0, gameField.width - size)
        params.topMargin = Random.nextInt(100, gameField.height - size)

        bug.layoutParams = params
        bug.setOnClickListener {
            gameField.removeView(bug)
            bugs.remove(bug)
            score += 10
            updateScore()
        }

        gameField.addView(bug)
        bugs.add(bug)
    }

    private fun updateScore() {
        tvScore.text = "Очки: $score"
    }
}
