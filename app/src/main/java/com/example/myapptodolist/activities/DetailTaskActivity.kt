package com.example.myapptodolist.activities

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.myapptodolist.R
import com.example.myapptodolist.data.TaskRepository
import com.example.myapptodolist.models.Task
import com.example.myapptodolist.models.TaskCard

class DetailTaskActivity : AppCompatActivity() {

    private var taskPosition: Int = -1
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        // --- Header ---
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        // --- Ambil TextView ---
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvDate = findViewById<TextView>(R.id.tvDate)

        // --- Ambil Task dari Intent ---
        taskPosition = intent.getIntExtra("TASK_POSITION", -1)
        if (taskPosition != -1) {
            task = TaskRepository.tasks[taskPosition]
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvDate.text = task.date
        }

        // --- Container Cards & Add Card ---
        val containerCards = findViewById<LinearLayout>(R.id.containerCards)
        val etNewCard = findViewById<EditText>(R.id.etNewCard)
        val btnAddCard = findViewById<Button>(R.id.btnAddNewCard)

        // Tampilkan semua card yang sudah ada
        task.cards.forEach { addCardToContainer(containerCards, it) }

        // Tombol ADD NEW CARD
        btnAddCard.setOnClickListener {
            val text = etNewCard.text.toString().trim()
            if (text.isNotEmpty()) {
                val newCard = TaskCard(text)
                task.cards.add(newCard)
                addCardToContainer(containerCards, newCard)
                etNewCard.text.clear()
            } else {
                Toast.makeText(this, "Masukkan teks card terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- Function menambahkan card ke LinearLayout ---
    private fun addCardToContainer(container: LinearLayout, card: TaskCard) {
        val cardView = CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 16) }
            radius = 16f
            cardElevation = 2f
            setCardBackgroundColor(resources.getColor(android.R.color.white, theme))
        }

        // Container horizontal: TextView + Edit + Delete
        val horizontalLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 16, 16, 16)
            gravity = android.view.Gravity.CENTER_VERTICAL
        }

        val tv = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            textSize = 16f
            text = card.text
            setTextColor(resources.getColor(android.R.color.black, theme))
        }

        val btnEdit = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(60, 60)
            setImageResource(android.R.drawable.ic_menu_edit)
            setBackgroundResource(0)
            setColorFilter(resources.getColor(android.R.color.holo_blue_dark, theme))
            setOnClickListener { showEditDialog(card, tv) }
        }

        val btnDelete = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(60, 60)
            setImageResource(android.R.drawable.ic_menu_delete)
            setBackgroundResource(0)
            setColorFilter(resources.getColor(android.R.color.holo_red_dark, theme))
            setOnClickListener {
                container.removeView(cardView)
                task.cards.remove(card)
            }
        }

        horizontalLayout.addView(tv)
        horizontalLayout.addView(btnEdit)
        horizontalLayout.addView(btnDelete)

        cardView.addView(horizontalLayout)
        container.addView(cardView)
    }

    // --- Function Edit Card ---
    private fun showEditDialog(card: TaskCard, tv: TextView) {
        val editText = EditText(this)
        editText.setText(card.text)

        AlertDialog.Builder(this)
            .setTitle("Edit Card")
            .setView(editText)
            .setPositiveButton("Save") { dialog, _ ->
                val newText = editText.text.toString().trim()
                if (newText.isNotEmpty()) {
                    card.text = newText
                    tv.text = newText
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
