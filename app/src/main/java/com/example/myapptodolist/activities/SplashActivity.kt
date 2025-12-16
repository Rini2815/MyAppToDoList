package com.example.myapptodolist.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapptodolist.R

class SplashActivity : AppCompatActivity() {

    private val PREFS_NAME = "TodoListPrefs"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 1500) // 1.5 detik
    }

    private fun checkLoginStatus() {
        val isLoggedIn = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .getBoolean(KEY_IS_LOGGED_IN, false)

        val intent = if (isLoggedIn) {
            Intent(this, HomeActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}