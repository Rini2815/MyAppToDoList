package com.example.myapptodolist.models

import java.io.Serializable

data class TaskCard(
    var text: String,
    var isDone: Boolean = false
) : Serializable
