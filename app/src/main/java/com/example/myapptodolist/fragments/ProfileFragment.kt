package com.example.myapptodolist.fragments

import android.content.Intent
import android.content.SharedPreferences
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
import com.example.myapptodolist.models.Task
import com.google.gson.Gson

class ProfileFragment : Fragment() {

    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        prefs = requireActivity().getSharedPreferences("USER_PREF", 0)

        val tvUsername = view.findViewById<TextView>(R.id.textViewUsername)
        val tvTugasSelesai = view.findViewById<TextView>(R.id.textViewTugasSelesai)
        val tvTugasTersedia = view.findViewById<TextView>(R.id.textViewTugasTersedia)

        val layoutPertanyaan = view.findViewById<LinearLayout>(R.id.layoutPertanyaan)
        val layoutTentang = view.findViewById<LinearLayout>(R.id.layoutTentangAplikasi)
        val layoutKeluar = view.findViewById<LinearLayout>(R.id.layoutKeluar)

        tvUsername.text = prefs.getString("USERNAME", "User")

        loadTaskSummary(tvTugasSelesai, tvTugasTersedia)

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
        view?.let {
            loadTaskSummary(
                it.findViewById(R.id.textViewTugasSelesai),
                it.findViewById(R.id.textViewTugasTersedia)
            )
        }
    }

    private fun loadTaskSummary(
        tvSelesai: TextView,
        tvTersedia: TextView
    ) {
        val json = prefs.getString("TASK_LIST", "[]") ?: "[]"

        val taskArray = Gson().fromJson(json, Array<Task>::class.java)
        val taskList = taskArray?.toList() ?: emptyList()

        tvSelesai.text = taskList.count { it.isDone }.toString()
        tvTersedia.text = taskList.count { !it.isDone }.toString()
    }

    private fun logout() {
        Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
        prefs.edit().clear().apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
