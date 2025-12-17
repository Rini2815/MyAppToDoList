package com.example.myapptodolist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapptodolist.R
import com.example.myapptodolist.activities.AddTaskActivity
import com.example.myapptodolist.activities.DetailTaskActivity
import com.example.myapptodolist.adapters.TaskAdapter
import com.example.myapptodolist.data.TaskRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var rvTodayTasks: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var adapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTodayTasks = view.findViewById(R.id.rvTodayTasks)
        fabAddTask = view.findViewById(R.id.fabAddTask)

        adapter = TaskAdapter(TaskRepository.tasks) { task ->
            val intent = Intent(requireContext(), DetailTaskActivity::class.java)
            intent.putExtra("TASK_TITLE", task.title)
            startActivity(intent)
        }

        rvTodayTasks.layoutManager = LinearLayoutManager(requireContext())
        rvTodayTasks.adapter = adapter

        // FAB → Add Task
        fabAddTask.setOnClickListener {
            startActivity(Intent(requireContext(), AddTaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
