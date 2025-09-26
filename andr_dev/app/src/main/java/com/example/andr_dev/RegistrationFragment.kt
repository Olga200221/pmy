package com.example.andr_dev

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
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

    private var selectedBirthDateMillis: Long = 0L

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

        // Spinner courses
        val courses = arrayOf("1 курс", "2 курс", "3 курс", "4 курс", "5 курс")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCourse.adapter = adapter

        // min/max dates
        val cal = Calendar.getInstance()
        val maxDate = cal.timeInMillis
        cal.add(Calendar.YEAR, -100)
        val minDate = cal.timeInMillis
        cvBirthDate.minDate = minDate
        cvBirthDate.maxDate = maxDate

        selectedBirthDateMillis = cvBirthDate.date
        tvBirthDate.text = formatDate(selectedBirthDateMillis)

        cvBirthDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val c = Calendar.getInstance()
            c.set(year, month, dayOfMonth, 0, 0, 0)
            c.set(Calendar.MILLISECOND, 0)
            selectedBirthDateMillis = c.timeInMillis
            tvBirthDate.text = formatDate(selectedBirthDateMillis)
        }

        btnPickDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnSubmit.setOnClickListener {
            val player = collectData()
            if (player != null) displayData(player)
        }
    }

    private fun collectData(): Player? {
        val fullName = etFullName.text.toString().trim()
        if (fullName.isEmpty()) {
            Toast.makeText(requireContext(), "Введите ФИО", Toast.LENGTH_SHORT).show()
            return null
        }

        val genderId = rgGender.checkedRadioButtonId
        val gender = if (genderId == R.id.rbMale) "Мужской" else if (genderId == R.id.rbFemale) "Женский" else ""
        if (gender.isEmpty()) {
            Toast.makeText(requireContext(), "Выберите пол", Toast.LENGTH_SHORT).show()
            return null
        }

        val course = spCourse.selectedItem.toString()
        val difficulty = sbDifficulty.progress

        val cal = Calendar.getInstance()
        cal.timeInMillis = selectedBirthDateMillis
        val birthDate = cal.time

        val zodiac = getZodiacSign(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))

        return Player(fullName, gender, course, difficulty, birthDate, zodiac)
    }

    private fun displayData(player: Player) {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
        val formattedDate = dateFormat.format(player.birthDate)
        val output = """
            ФИО: ${player.fullName}
            Пол: ${player.gender}
            Курс: ${player.course}
            Уровень сложности: ${player.difficulty}
            Дата рождения: $formattedDate
            Знак зодиака: ${getZodiacNameRu(player.zodiac)}
        """.trimIndent()

        tvOutput.text = output

        val zodiacDrawable = getZodiacDrawable(player.zodiac)
        if (zodiacDrawable != 0) {
            ivZodiac.setImageResource(zodiacDrawable)
            ivZodiac.visibility = View.VISIBLE
        } else {
            ivZodiac.visibility = View.GONE
        }
    }

    // возвращаем кодовое англ. имя для поиска картинки
    private fun getZodiacSign(month: Int, day: Int): String {
        return when {
            (month == 3 && day >= 21) || (month == 4 && day <= 19) -> "aries"
            (month == 4 && day >= 20) || (month == 5 && day <= 20) -> "taurus"
            (month == 5 && day >= 21) || (month == 6 && day <= 20) -> "gemini"
            (month == 6 && day >= 21) || (month == 7 && day <= 22) -> "cancer"
            (month == 7 && day >= 23) || (month == 8 && day <= 22) -> "leo"
            (month == 8 && day >= 23) || (month == 9 && day <= 22) -> "virgo"
            (month == 9 && day >= 23) || (month == 10 && day <= 22) -> "libra"
            (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "scorpio"
            (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "sagittarius"
            (month == 12 && day >= 22) || (month == 1 && day <= 19) -> "capricorn"
            (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "aquarius"
            else -> "pisces"
        }
    }

    // перевод англ. кодового имени в русское название
    private fun getZodiacNameRu(zodiac: String): String {
        return when (zodiac) {
            "aries" -> "Овен"
            "taurus" -> "Телец"
            "gemini" -> "Близнецы"
            "cancer" -> "Рак"
            "leo" -> "Лев"
            "virgo" -> "Дева"
            "libra" -> "Весы"
            "scorpio" -> "Скорпион"
            "sagittarius" -> "Стрелец"
            "capricorn" -> "Козерог"
            "aquarius" -> "Водолей"
            "pisces" -> "Рыбы"
            else -> zodiac
        }
    }

    private fun getZodiacDrawable(zodiac: String): Int {
        return when (zodiac) {
            "aries" -> R.drawable.aries
            "taurus" -> R.drawable.taurus
            "gemini" -> R.drawable.gemini
            "cancer" -> R.drawable.cancer
            "leo" -> R.drawable.leo
            "virgo" -> R.drawable.virgo
            "libra" -> R.drawable.libra
            "scorpio" -> R.drawable.scorpio
            "sagittarius" -> R.drawable.sagittarius
            "capricorn" -> R.drawable.capricorn
            "aquarius" -> R.drawable.aquarius
            "pisces" -> R.drawable.pisces
            else -> 0
        }
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = selectedBirthDateMillis
        val dpd = DatePickerDialog(
            requireContext(),
            { _, y, m, d ->
                val c = Calendar.getInstance()
                c.set(y, m, d, 0, 0, 0)
                c.set(Calendar.MILLISECOND, 0)
                selectedBirthDateMillis = c.timeInMillis
                cvBirthDate.date = selectedBirthDateMillis
                tvBirthDate.text = formatDate(selectedBirthDateMillis)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }

    private fun formatDate(timeMillis: Long): String {
        val df = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
        return df.format(Date(timeMillis))
    }
}
