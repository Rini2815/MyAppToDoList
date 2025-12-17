package com.example.myapptodolist.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapptodolist.R
import com.example.myapptodolist.data.TaskRepository
import com.example.myapptodolist.models.Task

class TaskAdapter(
    private val tasks: List<Task>,
    private val onItemClick: (Task, Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbDone: CheckBox = view.findViewById(R.id.cbDone)
        val tvTaskTitle: TextView = view.findViewById(R.id.tvTaskTitle)
        val tvTaskStatus: TextView = view.findViewById(R.id.tvTaskStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.tvTaskTitle.text = task.title

        // Update tampilan berdasarkan status isDone
        updateTaskUI(holder, task)

        // Set checkbox state tanpa trigger listener
        holder.cbDone.setOnCheckedChangeListener(null)
        holder.cbDone.isChecked = task.isDone

        // Set listener untuk checkbox
        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            // Update status task
            task.isDone = isChecked

            // Update tampilan
            updateTaskUI(holder, task)

            // Simpan perubahan ke SharedPreferences
            TaskRepository.saveTasks(holder.itemView.context)
        }

        // Klik item untuk buka detail
        holder.itemView.setOnClickListener {
            onItemClick(task, position)
        }
    }

    private fun updateTaskUI(holder: TaskViewHolder, task: Task) {
        if (task.isDone) {
            // Tugas selesai
            holder.tvTaskStatus.text = "Selesai"
            // Tambahkan strikethrough pada judul
            holder.tvTaskTitle.paintFlags = holder.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            // Bisa ubah warna jadi lebih pudar
            holder.tvTaskTitle.alpha = 0.6f
        } else {
            // Tugas belum selesai
            holder.tvTaskStatus.text = "Belum Selesai"
            // Hapus strikethrough
            holder.tvTaskTitle.paintFlags = holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            // Kembalikan opacity normal
            holder.tvTaskTitle.alpha = 1.0f
        }
    }

    override fun getItemCount() = tasks.size
}