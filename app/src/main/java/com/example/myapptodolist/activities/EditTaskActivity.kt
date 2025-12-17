package com.example.myapptodolist.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapptodolist.R
import java.util.*

class EditTaskActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var switchFavorite: Switch
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var btnUpdate: Button

    private var selectedDate = ""
    private var selectedTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        supportActionBar?.hide()

        initViews()
        loadTaskData()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        switchFavorite = findViewById(R.id.switchFavorite)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        btnUpdate = findViewById(R.id.btnUpdate)
    }

    private fun loadTaskData() {
        etTitle.setText(intent.getStringExtra("TASK_TITLE") ?: "")
        etDescription.setText(intent.getStringExtra("TASK_DESCRIPTION") ?: "")

        selectedDate = intent.getStringExtra("TASK_DATE") ?: ""
        selectedTime = intent.getStringExtra("TASK_TIME") ?: ""

        etDate.setText(selectedDate)
        etTime.setText(selectedTime)
        switchFavorite.isChecked = intent.getBooleanExtra("TASK_IS_FAVORITE", false)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }
        etDate.setOnClickListener { showDatePicker() }
        etTime.setOnClickListener { showTimePicker() }
        btnUpdate.setOnClickListener { updateTask() }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, y, m, d ->
                val months = arrayOf("Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agu","Sep","Okt","Nov","Des")
                selectedDate = "$d ${months[m]} $y"
                etDate.setText(selectedDate)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, h, m ->
                selectedTime = String.format("%02d:%02d", h, m)
                etTime.setText(selectedTime)
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun updateTask() {
        if (etTitle.text.isNullOrBlank()) {
            etTitle.error = "Judul tidak boleh kosong"
            return
        }

        // Kirim data kembali ke Activity sebelumnya
        val resultIntent = Intent().apply {
            putExtra("TASK_POSITION", intent.getIntExtra("TASK_POSITION", -1))
            putExtra("TASK_TITLE", etTitle.text.toString())
            putExtra("TASK_DESCRIPTION", etDescription.text.toString())
            putExtra("TASK_DATE", etDate.text.toString())
            putExtra("TASK_TIME", etTime.text.toString())
            putExtra("TASK_IS_FAVORITE", switchFavorite.isChecked)
        }

        setResult(RESULT_OK, resultIntent)
        Toast.makeText(this, "Tugas berhasil diupdate", Toast.LENGTH_SHORT).show()
        finish()
    }
}