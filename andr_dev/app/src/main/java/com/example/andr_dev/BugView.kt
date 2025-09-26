package com.example.andr_dev

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView

enum class BugRarity(val scoreMultiplier: Int) {
    COMMON(1),
    UNCOMMON(2),
    RARE(3),
    EPIC(5),
    LEGENDARY(10),
    MYTHIC(20),
    UNIQUE(30)
}

class BugView(context: Context, drawableRes: Int, val scoreValue: Int) : AppCompatImageView(context) {
    init {
        setImageResource(drawableRes)
    }
}
