package com.example.andr_dev

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndrDevApp : Application()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Регистрация"
                1 -> "Правила"
                2 -> "Авторы"
                3 -> "Настройки"
                4 -> "Рекорды"
                else -> ""
            }
        }.attach()

        val selectedTab = intent.getIntExtra("selected_tab", 0)
        viewPager.currentItem = selectedTab
    }
}