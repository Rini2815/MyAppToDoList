package com.example.myapptodolist.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapptodolist.R
import com.example.myapptodolist.data.TaskRepository

class ProfileActivity : AppCompatActivity() {

    private lateinit var imageViewProfile: ImageView
    private lateinit var textViewUsername: TextView
    private lateinit var textViewTugasSelesai: TextView
    private lateinit var textViewTugasTersedia: TextView

    private lateinit var layoutPertanyaan: LinearLayout
    private lateinit var layoutTentangAplikasi: LinearLayout
    private lateinit var layoutKeluar: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initViews()
        setProfileData()
        setMenuClickListeners()
    }

    override fun onResume() {
        super.onResume()
        updateTaskSummary()
    }

    private fun initViews() {
        imageViewProfile = findViewById(R.id.imageViewProfile)
        textViewUsername = findViewById(R.id.textViewUsername)
        textViewTugasSelesai = findViewById(R.id.textViewTugasSelesai)
        textViewTugasTersedia = findViewById(R.id.textViewTugasTersedia)

        layoutPertanyaan = findViewById(R.id.layoutPertanyaan)
        layoutTentangAplikasi = findViewById(R.id.layoutTentangAplikasi)
        layoutKeluar = findViewById(R.id.layoutKeluar)
    }

    private fun setProfileData() {
        textViewUsername.text = "inesfarah"
        updateTaskSummary()
    }

    private fun updateTaskSummary() {
        // Hitung total tugas tersedia
        val tugasTersedia = TaskRepository.tasks.size

        // Untuk tugas selesai, set manual atau hitung dari checkbox
        // Karena belum ada isCompleted, kita set 0 atau hardcode
        val tugasSelesai = 0  // Ubah sesuai kebutuhan

        textViewTugasSelesai.text = tugasSelesai.toString()
        textViewTugasTersedia.text = tugasTersedia.toString()
    }

    private fun setMenuClickListeners() {
        layoutPertanyaan.setOnClickListener {
            showPertanyaanDialog()
        }

        layoutTentangAplikasi.setOnClickListener {
            showTentangAplikasiDialog()
        }

        layoutKeluar.setOnClickListener {
            showKeluarDialog()
        }

        imageViewProfile.setOnClickListener {
            Toast.makeText(this, "Ganti Foto Profil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPertanyaanDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Pertanyaan")
            setMessage("Apakah Anda memiliki pertanyaan?\n\nSilakan hubungi admin atau dosen pengampu untuk bantuan lebih lanjut.")
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            setNegativeButton("Hubungi Admin") { _, _ ->
                Toast.makeText(this@ProfileActivity, "Menghubungi Admin...", Toast.LENGTH_SHORT).show()
            }
            show()
        }
    }

    private fun showTentangAplikasiDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Tentang Aplikasi")
            setMessage("""
                Aplikasi Tugas Mahasiswa
                
                Versi: 1.0.0
                Developer: Tim IT
                
                Aplikasi ini dibuat untuk membantu mahasiswa mengelola tugas kuliah dengan lebih mudah dan terorganisir.
            """.trimIndent())
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun showKeluarDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Keluar")
            setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
            setPositiveButton("Ya") { _, _ ->
                Toast.makeText(this@ProfileActivity, "Berhasil keluar", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }
}