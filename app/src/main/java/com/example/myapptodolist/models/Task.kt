package com.example.myapptodolist.models

import java.io.Serializable

data class Task(
    val title: String,
    val description: String = "",
    val date: String = "",
    val isFavorite: Boolean = false,
    val isDone: Boolean = false
) : Serializable