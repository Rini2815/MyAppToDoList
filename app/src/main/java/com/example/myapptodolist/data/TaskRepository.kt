package com.example.myapptodolist.data

import android.content.Context
import com.example.myapptodolist.models.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TaskRepository {

    val tasks = mutableListOf<Task>()

    // Tambah task dan langsung simpan
    fun addTask(context: Context, task: Task) {
        tasks.add(task)
        saveTasks(context)
    }

    // Update task di posisi tertentu dan simpan
    fun updateTask(context: Context, position: Int, updatedTask: Task) {
        if (position in tasks.indices) {
            tasks[position] = updatedTask
            saveTasks(context)
        }
    }

    // Hapus task di posisi tertentu dan simpan
    fun deleteTask(context: Context, position: Int) {
        if (position in tasks.indices) {
            tasks.removeAt(position)
            saveTasks(context)
        }
    }

    fun getFavoriteTasks(): List<Task> {
        return tasks.filter { it.isFavorite }
    }

    // Simpan semua task ke SharedPreferences
    fun saveTasks(context: Context) {
        val prefs = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        val json = Gson().toJson(tasks)
        prefs.edit().putString("TASK_LIST", json).apply()
    }

    // Load task dari SharedPreferences
    fun loadTasks(context: Context) {
        val prefs = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        val json = prefs.getString("TASK_LIST", "[]") ?: "[]"
        val type = object : TypeToken<MutableList<Task>>() {}.type
        val loadedTasks: MutableList<Task> = Gson().fromJson(json, type) ?: mutableListOf()
        tasks.clear()
        tasks.addAll(loadedTasks)
    }
}
