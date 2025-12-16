package com.example.myapptodolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapptodolist.R
import com.example.myapptodolist.models.Task

class TaskAdapter(
    private val tasks: List<Task>,
    private val onClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTaskTitle)
        val status: TextView = view.findViewById(R.id.tvTaskStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.status.text = if (task.isDone) "Selesai" else "Belum Selesai"

        holder.itemView.setOnClickListener {
            onClick(task)
        }
    }

    override fun getItemCount(): Int = tasks.size
}