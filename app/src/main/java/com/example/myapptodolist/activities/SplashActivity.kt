package com.example.myapptodolist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapptodolist.R

class SplashActivity : AppCompatActivity() {

    // Samakan dengan LoginActivity
    private val PREFS_NAME = "USER_PREF"
    private val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 1500) // 1.5 detik
    }

    private fun checkLoginStatus() {
        val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

        val intent = if (isLoggedIn) {
            // Sudah login → HomeActivity
            Intent(this, HomeActivity::class.java)
        } else {
            // Belum login → LoginActivity
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
