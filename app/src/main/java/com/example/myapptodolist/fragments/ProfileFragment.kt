package com.example.myapptodolist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapptodolist.R
import com.example.myapptodolist.activities.LoginActivity
import com.example.myapptodolist.activities.PertanyaanActivity
import com.example.myapptodolist.activities.TentangAplikasiActivity
import com.example.myapptodolist.data.TaskRepository

class ProfileFragment : Fragment() {

    private lateinit var tvUsername: TextView
    private lateinit var tvTugasSelesai: TextView
    private lateinit var tvTugasTersedia: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvUsername = view.findViewById(R.id.textViewUsername)
        tvTugasSelesai = view.findViewById(R.id.textViewTugasSelesai)
        tvTugasTersedia = view.findViewById(R.id.textViewTugasTersedia)

        val layoutPertanyaan = view.findViewById<LinearLayout>(R.id.layoutPertanyaan)
        val layoutTentang = view.findViewById<LinearLayout>(R.id.layoutTentangAplikasi)
        val layoutKeluar = view.findViewById<LinearLayout>(R.id.layoutKeluar)

        // Tampilkan username
        val prefs = requireActivity().getSharedPreferences("USER_PREF", 0)
        tvUsername.text = prefs.getString("USERNAME", "User")

        // Load task dari repository dan update summary
        updateTaskSummary()

        layoutPertanyaan.setOnClickListener {
            startActivity(Intent(requireContext(), PertanyaanActivity::class.java))
        }

        layoutTentang.setOnClickListener {
            startActivity(Intent(requireContext(), TentangAplikasiActivity::class.java))
        }

        layoutKeluar.setOnClickListener {
            logout()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Reload tasks dan update summary setiap fragment kembali aktif
        updateTaskSummary()
    }

    private fun updateTaskSummary() {
        // Load tasks terbaru dari SharedPreferences
        TaskRepository.loadTasks(requireContext())

        val tasks = TaskRepository.tasks

        // Hitung tugas selesai (isDone = true)
        val tugasSelesai = tasks.count { it.isDone }

        // Hitung tugas belum selesai (isDone = false)
        val tugasBelumSelesai = tasks.count { !it.isDone }

        // Update UI dengan angka terbaru
        tvTugasSelesai.text = tugasSelesai.toString()
        tvTugasTersedia.text = tugasBelumSelesai.toString()
    }

    private fun logout() {
        Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
        val prefs = requireActivity().getSharedPreferences("USER_PREF", 0)
        prefs.edit().clear().apply()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}