package com.example.myapptodolist.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapptodolist.R
import com.example.myapptodolist.adapters.TaskAdapter
import com.example.myapptodolist.data.TaskRepository

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var rvFavoriteTasks: RecyclerView
    private lateinit var adapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavoriteTasks = view.findViewById(R.id.rvFavoriteTasks)

        // Adapter WAJIB pakai (Task, Int)
        adapter = TaskAdapter(TaskRepository.getFavoriteTasks()) { task, _ ->
            // optional: nanti bisa buka DetailTaskActivity
        }

        rvFavoriteTasks.layoutManager = LinearLayoutManager(requireContext())
        rvFavoriteTasks.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
