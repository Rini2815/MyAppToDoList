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

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        val layoutPertanyaan = view.findViewById<LinearLayout>(R.id.layoutPertanyaan)
        val layoutTentang = view.findViewById<LinearLayout>(R.id.layoutTentang)
        val layoutLogout = view.findViewById<LinearLayout>(R.id.layoutLogout)

        tvUsername.text = "inesfarah"

        layoutPertanyaan.setOnClickListener {
            startActivity(Intent(requireContext(), PertanyaanActivity::class.java))
        }

        layoutTentang.setOnClickListener {
            startActivity(Intent(requireContext(), TentangAplikasiActivity::class.java))
        }

        layoutLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        return view
    }
}
