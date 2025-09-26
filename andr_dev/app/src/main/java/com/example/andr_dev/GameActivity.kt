package com.example.andr_dev

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var gameField: FrameLayout
    private lateinit var tvScore: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnRules: Button
    private lateinit var btnAuthors: Button
    private lateinit var btnSettings: Button
    private lateinit var btnExit: Button
    private lateinit var sharedPreferences: SharedPreferences

    private var score = 0
    private var gameDuration = 60000L // Значение по умолчанию
    private var maxBugs = 5 // Значение по умолчанию
    private var bugSpeed = 3000L // Значение по умолчанию
    private var bonusInterval = 5000L // Значение по умолчанию

    private lateinit var gameTimer: CountDownTimer

    private val bugDrawables = listOf(
        R.drawable.bug1, // COMMON
        R.drawable.bug2, // UNCOMMON
        R.drawable.bug3, // RARE
        R.drawable.bug4, // EPIC
        R.drawable.bug5, // LEGENDARY
        R.drawable.bug6, // MYTHIC
        R.drawable.bug7  // UNIQUE
    )

    private val bugRarities = listOf(
        BugRarity.COMMON,
        BugRarity.UNCOMMON,
        BugRarity.RARE,
        BugRarity.EPIC,
        BugRarity.LEGENDARY,
        BugRarity.MYTHIC,
        BugRarity.UNIQUE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        sharedPreferences = getSharedPreferences("GameSettings", MODE_PRIVATE)
        gameField = findViewById(R.id.gameField)
        tvScore = findViewById(R.id.tvScore)
        tvTimer = findViewById(R.id.tvTimer)
        btnRules = findViewById(R.id.btnRules)
        btnAuthors = findViewById(R.id.btnAuthors)
        btnSettings = findViewById(R.id.btnSettings)
        btnExit = findViewById(R.id.btnExit)

        // Загрузка настроек
        maxBugs = sharedPreferences.getInt("maxBugs", 5)
        bugSpeed = sharedPreferences.getLong("bugSpeed", 3000L)
        bonusInterval = sharedPreferences.getLong("bonusInterval", 5000L)
        gameDuration = sharedPreferences.getLong("gameDuration", 60000L)

        // обработка промаха
        gameField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && gameField.visibility == View.VISIBLE) {
                score -= 1
                updateScore()
            }
            true
        }

        // Настройка кнопок меню
        setupMenuButtons()

        startGame()
    }

    private fun setupMenuButtons() {
        btnRules.setOnClickListener {
            endGame()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selected_tab", 1) // Правила
            startActivity(intent)
        }

        btnAuthors.setOnClickListener {
            endGame()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selected_tab", 2) // Авторы
            startActivity(intent)
        }

        btnSettings.setOnClickListener {
            endGame()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selected_tab", 3) // Настройки
            startActivity(intent)
        }

        btnExit.setOnClickListener {
            endGame()
            finish()
        }
    }

    private fun startGame() {
        score = 0
        updateScore()

        // таймер игры
        gameTimer = object : CountDownTimer(gameDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = "Время: ${millisUntilFinished / 1000} c"
                if (gameField.childCount < maxBugs) {
                    spawnBug()
                }
                // Простая реализация бонусов
                if (millisUntilFinished % bonusInterval == 0L) {
                    spawnBonus()
                }
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameTimer.start()
    }

    private fun spawnBug() {
        val bugIndex = Random.nextInt(bugDrawables.size)
        val rarity = bugRarities[bugIndex]
        val bug = BugView(this, bugDrawables[bugIndex], rarity.scoreMultiplier * (bugIndex + 1))

        val size = 150
        val maxX = gameField.width - size
        val maxY = gameField.height - size

        if (maxX <= 0 || maxY <= 0) return

        val startX = Random.nextInt(maxX).toFloat()
        val startY = Random.nextInt(maxY).toFloat()

        bug.layoutParams = FrameLayout.LayoutParams(size, size)
        bug.x = startX
        bug.y = startY

        bug.setOnClickListener {
            score += bug.scoreValue
            updateScore()
            gameField.removeView(bug)
        }

        gameField.addView(bug)

        // анимация движения
        val endX = Random.nextInt(maxX).toFloat()
        val endY = Random.nextInt(maxY).toFloat()

        val animX = ObjectAnimator.ofFloat(bug, "x", startX, endX).apply {
            duration = bugSpeed
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }
        val animY = ObjectAnimator.ofFloat(bug, "y", startY, endY).apply {
            duration = bugSpeed
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }

        animX.start()
        animY.start()
    }

    private fun spawnBonus() {
        // Простая реализация бонуса
        score += 5 // Бонусные очки
        updateScore()
        Toast.makeText(this, "Бонус! +5 очков", Toast.LENGTH_SHORT).show()
    }

    private fun updateScore() {
        tvScore.text = "Очки: $score"
    }

    private fun endGame() {
        gameTimer.cancel()
        // Остановить все анимации жуков
        for (i in 0 until gameField.childCount) {
            val child = gameField.getChildAt(i)
            if (child is BugView) {
                child.clearAnimation()
            }
        }
        Toast.makeText(this, "Игра окончена! Итог: $score", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        endGame()
        super.onBackPressed()
    }
}