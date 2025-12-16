package com.example.myapptodolist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.myapptodolist.MainActivity
import com.example.myapptodolist.R

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnDaftar: AppCompatButton
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "TodoListPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        cbTerms = findViewById(R.id.cbTerms)
        btnDaftar = findViewById(R.id.btnDaftar)
    }

    private fun setupListeners() {
        btnDaftar.setOnClickListener {
            handleLogin()
        }
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
                Toast.makeText(
                    this,
                    "Harap setujui Syarat dan Ketentuan",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        if (validateCredentials(username, password)) {
            saveLoginStatus(username)
            Toast.makeText(
                this,
                "Login berhasil! Selamat datang, $username",
                Toast.LENGTH_SHORT
            ).show()
            navigateToMain()
        } else {
            Toast.makeText(
                this,
                "Username atau Password salah!",
                Toast.LENGTH_LONG
            ).show()
            etPassword.text.clear()
        }
    }

    private fun validateCredentials(username: String, password: String): Boolean {
        return (username == "admin" && password == "admin123") ||
                (username == "user" && password == "user123")
    }

    private fun saveLoginStatus(username: String) {
        sharedPreferences.edit().apply {
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

    override fun onBackPressed() {
        finishAffinity()
    }
}