package com.example.myapptodolist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.myapptodolist.MainActivity
import com.example.myapptodolist.R

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnDaftar: AppCompatButton
    private lateinit var prefs: SharedPreferences

    companion object {
        const val PREF_NAME = "USER_PREF"
        const val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"
        const val KEY_USERNAME = "USERNAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        initViews()
        setupListeners()
        setupBackPress()
    }

    private fun setupBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    private fun initViews() {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        cbTerms = findViewById(R.id.cbTerms)
        btnDaftar = findViewById(R.id.btnDaftar)
    }

    private fun setupListeners() {
        btnDaftar.setOnClickListener { handleLogin() }
    }

    private fun handleLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        etUsername.error = null
        etPassword.error = null

        when {
            username.isEmpty() -> {
                etUsername.error = "Username tidak boleh kosong"
                etUsername.requestFocus()
                return
            }
            password.isEmpty() -> {
                etPassword.error = "Password tidak boleh kosong"
                etPassword.requestFocus()
                return
            }
            !cbTerms.isChecked -> {
                Toast.makeText(this, "Harap setujui Syarat dan Ketentuan", Toast.LENGTH_SHORT).show()
                return
            }
        }

        saveLogin(username)

        Toast.makeText(this, "Login berhasil! Selamat datang, $username", Toast.LENGTH_SHORT).show()
        navigateToMain()
    }

    private fun saveLogin(username: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USERNAME, username)
            apply()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}