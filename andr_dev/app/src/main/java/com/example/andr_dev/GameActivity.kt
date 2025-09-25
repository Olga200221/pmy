package com.example.andr_dev

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
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
import com.example.andr_dev.data.ScoreDao
import com.example.andr_dev.data.ScoreEntity
import org.koin.android.ext.android.inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GameActivity : AppCompatActivity() {

    private lateinit var gameField: FrameLayout
    private lateinit var tvScore: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnRules: Button
    private lateinit var btnAuthors: Button
    private lateinit var btnSettings: Button
    private lateinit var btnExit: Button
    private val scoreDao: ScoreDao by inject()

    private var score = 0
    private var gameDuration = 60000L
    private var maxBugs = 5
    private var bugSpeed = 3000L
    private var bonusInterval = 5000L
    private var currentPlayerId: Long = 0L
    private var difficulty: Int = 5 // Из настроек

    private lateinit var gameTimer: CountDownTimer
    private val sharedPreferences: SharedPreferences by lazy { getSharedPreferences("GameSettings", MODE_PRIVATE) }

    // ... (остальные поля и методы как раньше)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        currentPlayerId = prefs.getLong("current_player_id", 0L)
        if (currentPlayerId == 0L) {
            Toast.makeText(this, "Сначала зарегистрируйтесь", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Загрузка настроек
        maxBugs = sharedPreferences.getInt("maxBugs", 5)
        bugSpeed = sharedPreferences.getLong("bugSpeed", 3000L)
        bonusInterval = sharedPreferences.getLong("bonusInterval", 5000L)
        gameDuration = sharedPreferences.getLong("gameDuration", 60000L)
        difficulty = sharedPreferences.getInt("difficulty", 5) // Из регистрации

        // Инициализация UI
        gameField = findViewById(R.id.gameField)
        tvScore = findViewById(R.id.tvScore)
        tvTimer = findViewById(R.id.tvTimer)
        btnRules = findViewById(R.id.btnRules)
        btnAuthors = findViewById(R.id.btnAuthors)
        btnSettings = findViewById(R.id.btnSettings)
        btnExit = findViewById(R.id.btnExit)

        gameField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                score -= 1
                updateScore()
            }
            true
        }

        setupMenuButtons()
        startGame()
    }

    private fun endGame() {
        gameTimer.cancel()
        for (i in 0 until gameField.childCount) {
            val child = gameField.getChildAt(i)
            if (child is BugView) {
                child.clearAnimation()
            }
        }

        // Сохранение очков в БД
        CoroutineScope(Dispatchers.IO).launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val scoreDate = dateFormat.format(Date())
            val scoreEntity = ScoreEntity(
                playerId = currentPlayerId,
                score = score,
                difficulty = difficulty,
                date = scoreDate
            )
            scoreDao.insertScore(scoreEntity)
        }

        Toast.makeText(this, "Игра окончена! Итог: $score", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("selected_tab", 4) // Переход к Рекордам
        startActivity(intent)
        finish()
    }

    // ... (остальные методы без изменений)
}