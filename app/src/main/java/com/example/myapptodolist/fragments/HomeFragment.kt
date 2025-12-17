package com.example.myapptodolist.fragments

import android.app.Activity
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

    private lateinit var rvTasks: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var adapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TaskRepository.loadTasks(requireContext())

        rvTasks = view.findViewById(R.id.rvTodayTasks)
        fabAddTask = view.findViewById(R.id.fabAddTask)

        setupRecyclerView()

        fabAddTask.setOnClickListener {
            val intent = Intent(requireContext(), AddTaskActivity::class.java)
            addTaskLauncher.launch(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(TaskRepository.tasks) { task, position ->
            openDetailTask(task, position)
        }

        rvTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }
    }

    private val addTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newTask = result.data?.getSerializableExtra("NEW_TASK") as? Task
            newTask?.let {
                TaskRepository.addTask(requireContext(), it)
                adapter.notifyItemInserted(TaskRepository.tasks.size - 1)
            }
        }
    }

    private val detailTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult

            val action = data.getStringExtra("ACTION")
            val position = data.getIntExtra("TASK_POSITION", -1)
            if (position == -1) return@registerForActivityResult

            when (action) {
                "DELETE" -> {
                    TaskRepository.deleteTask(requireContext(), position)
                    adapter.notifyItemRemoved(position)
                }
                "UPDATE" -> {
                    TaskRepository.loadTasks(requireContext())
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }

    private fun openDetailTask(task: Task, position: Int) {
        val intent = Intent(requireContext(), DetailTaskActivity::class.java).apply {
            putExtra("TASK_POSITION", position)
            putExtra("TASK_TITLE", task.title)
            putExtra("TASK_DESCRIPTION", task.description)
            putExtra("TASK_DATE", task.date)
            putExtra("TASK_IS_FAVORITE", task.isFavorite)
            putExtra("TASK_IS_DONE", task.isDone)
        }
        detailTaskLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        TaskRepository.loadTasks(requireContext())
        adapter.notifyDataSetChanged()
    }
}