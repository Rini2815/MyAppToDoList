package com.example.myapptodolist.data

import com.example.myapptodolist.models.Task

object TaskRepository {

    val tasks = mutableListOf<Task>()

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun getFavoriteTasks(): List<Task> {
        return tasks.filter { it.isFavorite }
    }
}
