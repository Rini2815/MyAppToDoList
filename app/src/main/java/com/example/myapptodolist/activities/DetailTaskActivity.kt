package com.example.myapptodolist.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import com.example.myapptodolist.R
import com.example.myapptodolist.data.TaskRepository
import com.example.myapptodolist.models.Task
import com.example.myapptodolist.models.TaskCard

class DetailTaskActivity : AppCompatActivity() {

    private var taskPosition: Int = -1
    private lateinit var task: Task
    private lateinit var btnMenu: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    saveAndFinish()
                }
            }
        )

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            saveAndFinish()
        }

        btnMenu = findViewById(R.id.btnMenu)
        btnMenu.setOnClickListener {
            showPopupMenu(it)
        }

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvDate = findViewById<TextView>(R.id.tvDate)

        taskPosition = intent.getIntExtra("TASK_POSITION", -1)

        if (taskPosition != -1 && taskPosition < TaskRepository.tasks.size) {
            task = TaskRepository.tasks[taskPosition]
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvDate.text = task.date
        } else {
            Toast.makeText(this, "Task tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val containerCards = findViewById<LinearLayout>(R.id.containerCards)
        val etNewCard = findViewById<EditText>(R.id.etNewCard)
        val btnAddCard = findViewById<Button>(R.id.btnAddNewCard)

        task.cards.forEach { card ->
            addCardToContainer(containerCards, card)
        }

        btnAddCard.setOnClickListener {
            val text = etNewCard.text.toString().trim()
            if (text.isNotEmpty()) {
                val newCard = TaskCard(text)
                task.cards.add(newCard)
                addCardToContainer(containerCards, newCard)
                etNewCard.text.clear()
                TaskRepository.saveTasks(this)
            } else {
                Toast.makeText(this, "Masukkan teks card terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.detail_task_menu, popup.menu)

        val favoriteItem = popup.menu.findItem(R.id.menu_favorite)
        favoriteItem.title =
            if (task.isFavorite) "Hapus dari Favorit" else "Tambah ke Favorit"

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    editTask()
                    true
                }
                R.id.menu_delete -> {
                    deleteTask()
                    true
                }
                R.id.menu_share -> {
                    shareTask()
                    true
                }
                R.id.menu_favorite -> {
                    toggleFavorite()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun editTask() {
        val intent = Intent(this, EditTaskActivity::class.java).apply {
            putExtra("TASK_POSITION", taskPosition)
            putExtra("TASK_TITLE", task.title)
            putExtra("TASK_DESCRIPTION", task.description)
            putExtra("TASK_DATE", task.date)
            putExtra("TASK_IS_FAVORITE", task.isFavorite)
        }
        startActivityForResult(intent, REQUEST_EDIT_TASK)
    }

    private fun deleteTask() {
        AlertDialog.Builder(this)
            .setTitle("Hapus Tugas")
            .setMessage("Apakah Anda yakin ingin menghapus tugas ini?")
            .setPositiveButton("Hapus") { _, _ ->
                TaskRepository.deleteTask(this, taskPosition)

                val resultIntent = Intent().apply {
                    putExtra("ACTION", "DELETE")
                    putExtra("TASK_POSITION", taskPosition)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                Toast.makeText(this, "Tugas berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun shareTask() {
        val shareText = buildString {
            append("📋 ${task.title}\n\n")
            if (task.description.isNotEmpty()) append("📝 ${task.description}\n\n")
            if (task.date.isNotEmpty()) append("📅 ${task.date}\n\n")
            if (task.cards.isNotEmpty()) {
                append("✅ Checklist:\n")
                task.cards.forEach {
                    val status = if (it.isDone) "☑" else "☐"
                    append("$status ${it.text}\n")
                }
            }
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, task.title)
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(Intent.createChooser(intent, "Bagikan tugas via"))
    }

    private fun toggleFavorite() {
        task.isFavorite = !task.isFavorite
        TaskRepository.saveTasks(this)

        Toast.makeText(
            this,
            if (task.isFavorite) "Ditambahkan ke Favorit" else "Dihapus dari Favorit",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun addCardToContainer(container: LinearLayout, card: TaskCard) {
        val cardView = CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            radius = 16f
            cardElevation = 2f
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(16, 16, 16, 16)
        }

        val tv = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            text = card.text
            textSize = 16f
        }

        val btnEdit = ImageButton(this).apply {
            setImageResource(android.R.drawable.ic_menu_edit)
            setBackgroundResource(0)
            setOnClickListener {
                showEditDialog(card, tv)
            }
        }

        val btnDelete = ImageButton(this).apply {
            setImageResource(android.R.drawable.ic_menu_delete)
            setBackgroundResource(0)
            setOnClickListener {
                container.removeView(cardView)
                task.cards.remove(card)
                TaskRepository.saveTasks(this@DetailTaskActivity)
            }
        }

        layout.addView(tv)
        layout.addView(btnEdit)
        layout.addView(btnDelete)
        cardView.addView(layout)
        container.addView(cardView)
    }

    private fun showEditDialog(card: TaskCard, tv: TextView) {
        val editText = EditText(this).apply {
            setText(card.text)
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Card")
            .setView(editText)
            .setPositiveButton("Simpan") { _, _ ->
                val newText = editText.text.toString().trim()
                if (newText.isNotEmpty()) {
                    card.text = newText
                    tv.text = newText
                    TaskRepository.saveTasks(this)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun saveAndFinish() {
        TaskRepository.saveTasks(this)

        val resultIntent = Intent().apply {
            putExtra("ACTION", "UPDATE")
            putExtra("TASK_POSITION", taskPosition)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_EDIT_TASK && resultCode == Activity.RESULT_OK && data != null) {
            val position = data.getIntExtra("TASK_POSITION", -1)
            if (position != -1 && position < TaskRepository.tasks.size) {
                task = TaskRepository.tasks[position]
                findViewById<TextView>(R.id.tvTitle).text = task.title
                findViewById<TextView>(R.id.tvDescription).text = task.description
                findViewById<TextView>(R.id.tvDate).text = task.date
            }
        }
    }

    companion object {
        private const val REQUEST_EDIT_TASK = 100
    }
}