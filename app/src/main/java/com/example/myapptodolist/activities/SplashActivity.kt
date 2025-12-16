package com.example.myapptodolist.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapptodolist.activities.HomeActivity
import com.example.myapptodolist.activities.LoginActivity
import com.example.myapptodolist.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val PREFS_NAME = "TodoListPrefs"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay splash screen using coroutine
        lifecycleScope.launch {
            delay(1500)
            moveNext()
        }
    }

    private fun moveNext() {
        val isLoggedIn = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .getBoolean(KEY_IS_LOGGED_IN, false)

        val nextActivity = if (isLoggedIn) HomeActivity::class.java else LoginActivity::class.java
        startActivity(Intent(this, nextActivity))
        finish()
    }
}