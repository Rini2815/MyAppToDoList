package com.example.myapptodolist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapptodolist.R
import com.example.myapptodolist.models.Task
import com.google.gson.Gson

class ProfileActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    private lateinit var tvUsername: TextView
    private lateinit var tvTugasSelesai: TextView
    private lateinit var tvTugasTersedia: TextView

    private lateinit var layoutPertanyaan: LinearLayout
    private lateinit var layoutTentang: LinearLayout
    private lateinit var layoutKeluar: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile) // pastikan nama file XML sesuai

        prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE)

        tvUsername = findViewById(R.id.textViewUsername)
        tvTugasSelesai = findViewById(R.id.textViewTugasSelesai)
        tvTugasTersedia = findViewById(R.id.textViewTugasTersedia)

        layoutPertanyaan = findViewById(R.id.layoutPertanyaan)
        layoutTentang = findViewById(R.id.layoutTentangAplikasi)
        layoutKeluar = findViewById(R.id.layoutKeluar)

        tvUsername.text = prefs.getString("USERNAME", "User")

        loadTaskSummary()

        layoutPertanyaan.setOnClickListener {
            startActivity(Intent(this, PertanyaanActivity::class.java))
        }

        layoutTentang.setOnClickListener {
            startActivity(Intent(this, TentangAplikasiActivity::class.java))
        }

        layoutKeluar.setOnClickListener {
            logout()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTaskSummary() // refresh setiap kali kembali ke activity
    }

    private fun loadTaskSummary() {
        val json = prefs.getString("TASK_LIST", "[]") ?: "[]"
        val taskArray = Gson().fromJson(json, Array<Task>::class.java)
        val taskList = taskArray?.toList() ?: emptyList()

        tvTugasSelesai.text = taskList.count { it.isDone }.toString()
        tvTugasTersedia.text = taskList.count { !it.isDone }.toString()
    }

    private fun logout() {
        Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
        prefs.edit().clear().apply()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
