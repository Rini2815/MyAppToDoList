package com.example.myapptodolist.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapptodolist.R
import com.example.myapptodolist.models.Task
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task) // XML yang terakhir pakai ScrollView + ConstraintLayout

        val edtTitle = findViewById<EditText>(R.id.edtTitle)
        val edtDesc = findViewById<EditText>(R.id.edtDesc)
        val cbFavorite = findViewById<CheckBox>(R.id.cbFavorite)
        val edtDate = findViewById<EditText>(R.id.edtDate)
        val btnCalendar = findViewById<ImageView>(R.id.btnCalendar)
        val btnSaveTask = findViewById<Button>(R.id.btnSaveTask)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        // Tombol kembali
        btnBack.setOnClickListener { finish() }

        // Date picker
        btnCalendar.setOnClickListener {
            val cal = Calendar.getInstance()
            val dpd = DatePickerDialog(
                this,
                { _, year, month, day ->
                    edtDate.setText("$day/${month + 1}/$year")
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        // Tombol Add Card → kirim data ke HomeFragment
        btnSaveTask.setOnClickListener {
            val title = edtTitle.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(this, "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = Task(
                title = title,
                description = edtDesc.text.toString().trim(),
                date = edtDate.text.toString().trim(),
                isFavorite = cbFavorite.isChecked
            )

            // Kirim task kembali ke HomeActivity/HomeFragment via intent
            val intent = intent
            intent.putExtra("newTask", task)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
