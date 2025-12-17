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
    private val taskList: MutableList<Task>,
    private val onClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTaskTitle)
        val status: TextView = view.findViewById(R.id.tvTaskStatus)
        val cbDone: CheckBox = view.findViewById(R.id.cbDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.title.text = task.title
        holder.status.text =
            if (task.isDone) "Selesai" else "Belum Selesai"

        // supaya ga bug recyclerview
        holder.cbDone.setOnCheckedChangeListener(null)
        holder.cbDone.isChecked = task.isDone

        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            task.isDone = isChecked
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            onClick(task)
        }
    }

    override fun getItemCount(): Int = taskList.size

    fun updateData(newList: List<Task>) {
        taskList.clear()
        taskList.addAll(newList)
        notifyDataSetChanged()
    }
}
