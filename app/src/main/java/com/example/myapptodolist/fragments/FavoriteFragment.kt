package com.example.myapptodolist.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapptodolist.R
import com.example.myapptodolist.adapters.FavoriteAdapter
import com.example.myapptodolist.data.TaskRepository

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var rvFavoriteTasks: RecyclerView
    private lateinit var adapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavoriteTasks = view.findViewById(R.id.rvFavoriteTasks)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        TaskRepository.loadTasks(requireContext())

        val favoriteTasks = TaskRepository.getFavoriteTasks()

        val favoriteTitles = favoriteTasks.map { it.title }.toMutableList()

        adapter = FavoriteAdapter(favoriteTitles)
        rvFavoriteTasks.layoutManager = LinearLayoutManager(requireContext())
        rvFavoriteTasks.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }
}