package com.example.myapptodolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapptodolist.R
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
        holder.tvTaskStatus.text = "Belum Selesai" // Hardcode dulu untuk testing
        holder.cbDone.isChecked = false

        // Klik item untuk edit
        holder.itemView.setOnClickListener {
            onItemClick(task, position)
        }
    }

    override fun getItemCount() = tasks.size
}