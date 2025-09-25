package com.example.andr_dev

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.andr_dev.data.PlayerDao
import com.example.andr_dev.data.PlayerEntity
import org.koin.android.ext.android.inject
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private lateinit var etFullName: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var spCourse: Spinner
    private lateinit var sbDifficulty: SeekBar
    private lateinit var cvBirthDate: CalendarView
    private lateinit var ivZodiac: ImageView
    private lateinit var btnSubmit: Button
    private lateinit var btnPickDate: Button
    private lateinit var tvOutput: TextView
    private lateinit var tvBirthDate: TextView
    private lateinit var btnSelectPlayer: Button
    private val playerDao: PlayerDao by inject()

    private var selectedBirthDateMillis: Long = 0L
    private var currentPlayer: PlayerEntity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etFullName = view.findViewById(R.id.etFullName)
        rgGender = view.findViewById(R.id.rgGender)
        spCourse = view.findViewById(R.id.spCourse)
        sbDifficulty = view.findViewById(R.id.sbDifficulty)
        cvBirthDate = view.findViewById(R.id.cvBirthDate)
        ivZodiac = view.findViewById(R.id.ivZodiac)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        btnPickDate = view.findViewById(R.id.btnPickDate)
        tvOutput = view.findViewById(R.id.tvOutput)
        tvBirthDate = view.findViewById(R.id.tvBirthDate)
        btnSelectPlayer = view.findViewById(R.id.btnSelectPlayer)

        // Настройка Spinner
        val courses = arrayOf("1 курс", "2 курс", "3 курс", "4 курс", "5 курс")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCourse.adapter = adapter

        // Диапазон дат
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -100)
        cvBirthDate.minDate = cal.timeInMillis
        cvBirthDate.maxDate = System.currentTimeMillis()

        // Обработчики
        btnPickDate.setOnClickListener { showDatePickerDialog() }
        btnSubmit.setOnClickListener { saveOrUpdatePlayer() }
        btnSelectPlayer.setOnClickListener { showPlayerSelection() }

        loadCurrentPlayer()
    }

    private fun loadCurrentPlayer() {
        val prefs = requireContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        val playerId = prefs.getLong("current_player_id", 0L)
        if (playerId > 0) {
            lifecycleScope.launch {
                currentPlayer = playerDao.getPlayerById(playerId)
                if (currentPlayer != null) {
                    fillForm(currentPlayer!!)
                    tvOutput.text = "Редактирование: ${currentPlayer!!.fullName}"
                }
            }
        }
    }

    private fun fillForm(player: PlayerEntity) {
        etFullName.setText(player.fullName)
        rgGender.check(if (player.gender == "Мужской") R.id.rbMale else R.id.rbFemale)
        spCourse.setSelection(player.course.replace(" курс", "").toInt() - 1)
        sbDifficulty.progress = player.difficulty
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(player.birthDate)
        if (date != null) {
            selectedBirthDateMillis = date.time
            cvBirthDate.date = selectedBirthDateMillis
            tvBirthDate.text = formatDate(selectedBirthDateMillis)
        }
        val zodiacDrawable = getZodiacDrawable(player.zodiac)
        if (zodiacDrawable != 0) {
            ivZodiac.setImageResource(zodiacDrawable)
            ivZodiac.visibility = View.VISIBLE
        }
    }

    private fun saveOrUpdatePlayer() {
        val fullName = etFullName.text.toString().trim()
        if (fullName.isEmpty()) return Toast.makeText(requireContext(), "Введите ФИО", Toast.LENGTH_SHORT).show()

        val gender = if (rgGender.checkedRadioButtonId == R.id.rbMale) "Мужской" else "Женский"
        if (rgGender.checkedRadioButtonId == -1) return Toast.makeText(requireContext(), "Выберите пол", Toast.LENGTH_SHORT).show()

        val course = spCourse.selectedItem.toString()
        val difficulty = sbDifficulty.progress
        val birthDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(selectedBirthDateMillis))
        val zodiac = getZodiacSign(Date(selectedBirthDateMillis))

        val player = PlayerEntity(
            id = currentPlayer?.id ?: 0L,
            fullName = fullName,
            gender = gender,
            course = course,
            difficulty = difficulty,
            birthDate = birthDateStr,
            zodiac = zodiac
        )

        lifecycleScope.launch {
            if (currentPlayer == null) {
                playerDao.insertPlayer(player)
            } else {
                playerDao.updatePlayer(player)
            }
            requireContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE).edit()
                .putLong("current_player_id", player.id).apply()
            Toast.makeText(requireContext(), "Игрок сохранен", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_registration_to_rules) // Или к игре
        }
    }

    private fun showPlayerSelection() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите игрока")
        lifecycleScope.launch {
            val players = playerDao.getAllPlayers().first()
            val names = players.map { it.fullName }.toTypedArray()
            builder.setItems(names) { _, which ->
                currentPlayer = players[which]
                fillForm(currentPlayer!!)
            }
            builder.setNegativeButton("Новый игрок") { _, _ -> currentPlayer = null }
            builder.setPositiveButton("Отмена") { dialog, _ -> dialog.dismiss() }
            builder.show()
        }
    }

    // Методы для зодиака, даты (из предыдущего кода)
    private fun getZodiacSign(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return when {
            (month == 3 && day >= 21) || (month == 4 && day <= 19) -> "Aries"
            // ... (остальные знаки)
            else -> "Pisces"
        }
    }

    private fun getZodiacDrawable(zodiac: String): Int {
        return when (zodiac) {
            "Aries" -> R.drawable.aries
            // ... (остальные)
            else -> 0
        }
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = selectedBirthDateMillis
        DatePickerDialog(
            requireContext(),
            { _, y, m, d ->
                cal.set(y, m, d)
                selectedBirthDateMillis = cal.timeInMillis
                cvBirthDate.date = selectedBirthDateMillis
                tvBirthDate.text = SimpleDateFormat("dd MMMM yyyy", Locale("ru")).format(cal.time)
                val zodiac = getZodiacSign(cal.time)
                val drawable = getZodiacDrawable(zodiac)
                if (drawable != 0) {
                    ivZodiac.setImageResource(drawable)
                    ivZodiac.visibility = View.VISIBLE
                }
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}