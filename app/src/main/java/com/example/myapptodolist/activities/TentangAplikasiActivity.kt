package com.example.myapptodolist.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapptodolist.R

class TentangAplikasiActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tentang_aplikasi)

        supportActionBar?.hide()

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener { finish() }
    }
}
