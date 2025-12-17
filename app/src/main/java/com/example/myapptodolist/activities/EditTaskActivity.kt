package com.example.myapptodolist.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapptodolist.R
import com.example.myapptodolist.data.TaskRepository
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
    private var taskPosition = -1

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
        taskPosition = intent.getIntExtra("TASK_POSITION", -1)

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
                selectedDate = "$d/${m + 1}/$y"
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

        if (taskPosition == -1 || taskPosition >= TaskRepository.tasks.size) {
            Toast.makeText(this, "Task tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Update task di repository
        val task = TaskRepository.tasks[taskPosition]
        task.title = etTitle.text.toString()
        task.description = etDescription.text.toString()
        task.date = etDate.text.toString()
        task.isFavorite = switchFavorite.isChecked

        // Simpan ke SharedPreferences
        TaskRepository.saveTasks(this)

        // Kirim data kembali
        val resultIntent = Intent().apply {
            putExtra("TASK_POSITION", taskPosition)
            putExtra("TASK_TITLE", task.title)
            putExtra("TASK_DESCRIPTION", task.description)
            putExtra("TASK_DATE", task.date)
            putExtra("TASK_TIME", selectedTime)
            putExtra("TASK_IS_FAVORITE", task.isFavorite)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        Toast.makeText(this, "Tugas berhasil diupdate", Toast.LENGTH_SHORT).show()
        finish()
    }
}