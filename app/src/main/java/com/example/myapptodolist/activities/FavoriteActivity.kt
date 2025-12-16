package com.example.myapptodolist.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapptodolist.adapters.FavoriteAdapter
import com.example.myapptodolist.R

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val rvFavorite = findViewById<RecyclerView>(R.id.rvFavorite)

        // Data dummy (tanpa database)
        val favoriteList = mutableListOf(
            "Pemrograman Mobile",
            "Sistem Cerdas"
        )

        rvFavorite.layoutManager = LinearLayoutManager(this)
        rvFavorite.adapter = FavoriteAdapter(favoriteList)
    }
}