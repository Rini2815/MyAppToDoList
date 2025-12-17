package com.example.myapptodolist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapptodolist.R
import com.example.myapptodolist.activities.AddTaskActivity
import com.example.myapptodolist.activities.DetailTaskActivity
import com.example.myapptodolist.adapters.TaskAdapter
import com.example.myapptodolist.data.TaskRepository
import com.example.myapptodolist.models.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var rvTodayTasks: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var adapter: TaskAdapter

    // Launcher hasil dari DetailTaskActivity
    private val detailTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.let { data ->
                val action = data.getStringExtra("ACTION")
                val position = data.getIntExtra("TASK_POSITION", -1)

                if (position == -1 || position >= TaskRepository.tasks.size) return@let

                when (action) {

                    "DELETE" -> {
                        TaskRepository.tasks.removeAt(position)
                        adapter.notifyItemRemoved(position)
                        adapter.notifyItemRangeChanged(
                            position,
                            TaskRepository.tasks.size
                        )
                    }

                    "UPDATE" -> {
                        val oldTask = TaskRepository.tasks[position]

                        val updatedTask = oldTask.copy(
                            title = data.getStringExtra("TASK_TITLE") ?: "",
                            description = data.getStringExtra("TASK_DESCRIPTION") ?: "",
                            date = data.getStringExtra("TASK_DATE") ?: "",
                            isFavorite = data.getBooleanExtra("TASK_IS_FAVORITE", false)
                        )

                        TaskRepository.tasks[position] = updatedTask
                        adapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTodayTasks = view.findViewById(R.id.rvTodayTasks)
        fabAddTask = view.findViewById(R.id.fabAddTask)

        // ⚠️ WAJIB 2 PARAMETER: (Task, Int)
        adapter = TaskAdapter(TaskRepository.tasks) { task, _ ->
            openDetailTask(task)
        }

        rvTodayTasks.layoutManager = LinearLayoutManager(requireContext())
        rvTodayTasks.adapter = adapter

        fabAddTask.setOnClickListener {
            startActivity(Intent(requireContext(), AddTaskActivity::class.java))
        }
    }

    private fun openDetailTask(task: Task) {
        val position = TaskRepository.tasks.indexOf(task)

        val intent = Intent(requireContext(), DetailTaskActivity::class.java).apply {
            putExtra("TASK_POSITION", position)
            putExtra("TASK_TITLE", task.title)
            putExtra("TASK_DESCRIPTION", task.description)
            putExtra("TASK_DATE", task.date)
            putExtra("TASK_IS_FAVORITE", task.isFavorite)
        }

        detailTaskLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
