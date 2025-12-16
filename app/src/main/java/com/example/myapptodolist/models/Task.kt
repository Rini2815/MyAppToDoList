package com.example.myapptodolist.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val title: String,
    val description: String = "",
    val date: String = "",
    var isDone: Boolean = false,
    var isFavorite: Boolean = false
) : Parcelable
