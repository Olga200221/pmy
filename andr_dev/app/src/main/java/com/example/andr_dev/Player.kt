package com.example.andr_dev

import java.util.Date

data class Player(
    val fullName: String,
    val gender: String,
    val course: String,
    val difficulty: Int,
    val birthDate: Date,
    val zodiac: String
)
