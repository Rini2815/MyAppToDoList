package com.example.myapptodolist.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapptodolist.R
import com.example.myapptodolist.models.Task
import com.example.myapptodolist.adapters.TaskAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val taskList = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvTodayTasks)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAddTask)
        val emptyState = view.findViewById<View>(R.id.emptyState)

        recyclerView.setupAdapter(taskList) { position, isChecked ->
            taskList[position].isDone = isChecked
        }

        emptyState.updateVisibility(taskList)

        fab.setOnClickListener {
            taskList.add(Task("Tugas baru ${taskList.size + 1}"))
            adapter.notifyItemInserted(taskList.size - 1)
            emptyState.updateVisibility(taskList)
        }
    }

    private fun RecyclerView.setupAdapter(
        list: MutableList<Task>,
        onCheckChanged: (position: Int, isChecked: Boolean) -> Unit
    ) {
        adapter = TaskAdapter(list, onCheckChanged).also { this@HomeFragment.adapter = it }
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun View.updateVisibility(list: List<Task>) {
        visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }
}