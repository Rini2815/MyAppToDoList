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

class ProfileActivity : AppCompatActivity() {

    // Deklarasi variabel view
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

        // Inisialisasi view dengan findViewById
        initViews()

        // Set data profil
        setProfileData()

        // Set onclick listener untuk menu
        setMenuClickListeners()
    }

    private fun initViews() {
        // Header profil
        imageViewProfile = findViewById(R.id.imageViewProfile)
        textViewUsername = findViewById(R.id.textViewUsername)

        // Ringkasan tugas
        textViewTugasSelesai = findViewById(R.id.textViewTugasSelesai)
        textViewTugasTersedia = findViewById(R.id.textViewTugasTersedia)

        // Menu layout
        layoutPertanyaan = findViewById(R.id.layoutPertanyaan)
        layoutTentangAplikasi = findViewById(R.id.layoutTentangAplikasi)
        layoutKeluar = findViewById(R.id.layoutKeluar)
    }

    private fun setProfileData() {
        // Set data username
        textViewUsername.text = "inesfarah"

        // Set data tugas
        textViewTugasSelesai.text = "1"
        textViewTugasTersedia.text = "2"

        // Set profile image (opsional, jika punya gambar)
        // imageViewProfile.setImageResource(R.drawable.profile_photo)
    }

    private fun setMenuClickListeners() {
        // OnClick untuk Pertanyaan
        layoutPertanyaan.setOnClickListener {
            showPertanyaanDialog()
        }

        // OnClick untuk Tentang Aplikasi
        layoutTentangAplikasi.setOnClickListener {
            showTentangAplikasiDialog()
        }

        // OnClick untuk Keluar
        layoutKeluar.setOnClickListener {
            showKeluarDialog()
        }

        // OnClick untuk profile image (opsional)
        imageViewProfile.setOnClickListener {
            Toast.makeText(this, "Ganti Foto Profil", Toast.LENGTH_SHORT).show()
            // Bisa tambahkan fungsi untuk ganti foto profil
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
                // Tambahkan intent untuk email atau WA
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
                // Logout dan kembali ke LoginActivity
                Toast.makeText(this@ProfileActivity, "Berhasil keluar", Toast.LENGTH_SHORT).show()

                // Clear session atau shared preferences jika ada
                // val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)
                // prefs.edit().clear().apply()

                // Intent ke LoginActivity
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

    // Method untuk update jumlah tugas (bisa dipanggil dari activity lain)
    fun updateTugasSelesai(jumlah: Int) {
        textViewTugasSelesai.text = jumlah.toString()
    }

    fun updateTugasTersedia(jumlah: Int) {
        textViewTugasTersedia.text = jumlah.toString()
    }
}