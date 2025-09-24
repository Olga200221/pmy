package com.example.andr_dev

import android.content.Context
import android.widget.ImageView

class BugView(context: Context) : ImageView(context) {
    init {
        setImageResource(R.drawable.bug) // картинка насекомого (нужно добавить bug.png в drawable)
    }
}
