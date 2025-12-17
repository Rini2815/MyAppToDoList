package com.example.myapptodolist.models

import java.io.Serializable

data class Task(
    var title: String,
    var description: String = "",
    var date: String = "",

    var isFavorite: Boolean = false,
    var isDone: Boolean = false,

    val cards: MutableList<TaskCard> = mutableListOf()
) : Serializable