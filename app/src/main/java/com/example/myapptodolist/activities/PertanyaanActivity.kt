package com.example.myapptodolist.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapptodolist.R

class PertanyaanActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pertanyaan)

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener { finish() }
    }
}
