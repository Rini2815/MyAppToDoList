package com.example.myapptodolist.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapptodolist.R

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvDate: TextView
    private lateinit var containerCards: LinearLayout
    private lateinit var etNewCard: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var btnBack: ImageButton
    private lateinit var btnMenu: ImageButton

    private val cardList = mutableListOf<String>()
    private var isFavorite = false
    private var taskPosition = -1

    // Launcher untuk EditTaskActivity
    private val editTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                tvTitle.text = data.getStringExtra("TASK_TITLE") ?: tvTitle.text
                tvDescription.text = data.getStringExtra("TASK_DESCRIPTION") ?: tvDescription.text
                tvDate.text = data.getStringExtra("TASK_DATE") ?: tvDate.text
                isFavorite = data.getBooleanExtra("TASK_IS_FAVORITE", isFavorite)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        supportActionBar?.hide()

        initViews()
        loadTaskData()
        setupListeners()
    }

    private fun initViews() {
        tvTitle = findViewById(R.id.tvTitle)
        tvDescription = findViewById(R.id.tvDescription)
        tvDate = findViewById(R.id.tvDate)
        containerCards = findViewById(R.id.containerCards)
        etNewCard = findViewById(R.id.etNewCard)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
        btnBack = findViewById(R.id.btnBack)
        btnMenu = findViewById(R.id.btnMenu)
    }

    private fun loadTaskData() {
        tvTitle.text = intent.getStringExtra("TASK_TITLE") ?: "Judul Tugas"
        tvDescription.text = intent.getStringExtra("TASK_DESCRIPTION") ?: "Deskripsi tugas"
        tvDate.text = intent.getStringExtra("TASK_DATE") ?: "12 Jan 2025"

        isFavorite = intent.getBooleanExtra("TASK_IS_FAVORITE", false)
        taskPosition = intent.getIntExtra("TASK_POSITION", -1)

        val cards = intent.getStringArrayListExtra("TASK_CARDS")
        if (!cards.isNullOrEmpty()) {
            cardList.addAll(cards)
        } else {
            cardList.add("UI/UX Design")
            cardList.add("Implementasi ke Aplikasi")
        }

        displayCards()
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }
        btnCancel.setOnClickListener { finish() }

        btnMenu.setOnClickListener { showMenuDialog() }
        btnSave.setOnClickListener { saveTask() }

        etNewCard.setOnEditorActionListener { _, _, _ ->
            addNewCard()
            true
        }
    }

    private fun displayCards() {
        containerCards.removeAllViews()
        cardList.forEachIndexed { index, text ->
            addCardView(text, index)
        }
    }

    private fun addCardView(text: String, position: Int) {
        val cardView = LayoutInflater.from(this)
            .inflate(R.layout.item_task_card, containerCards, false)

        val checkbox = cardView.findViewById<CheckBox>(R.id.cbCard)
        val tvCard = cardView.findViewById<TextView>(R.id.tvCardText)
        val btnDelete = cardView.findViewById<ImageButton>(R.id.btnDeleteCard)

        tvCard.text = text

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "$text selesai!", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            showDeleteCardConfirmation(position)
        }

        containerCards.addView(cardView)
    }

    private fun addNewCard() {
        val text = etNewCard.text.toString().trim()
        if (text.isEmpty()) {
            Toast.makeText(this, "Card tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        cardList.add(text)
        etNewCard.text.clear()
        displayCards()
        Toast.makeText(this, "Card berhasil ditambahkan", Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteCardConfirmation(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Card")
            .setMessage("Yakin ingin menghapus \"${cardList[position]}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                cardList.removeAt(position)
                displayCards()
                Toast.makeText(this, "Card berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showMenuDialog() {
        val options = if (isFavorite)
            arrayOf("Edit Tugas", "Hapus dari Favorit", "Bagikan", "Hapus")
        else
            arrayOf("Edit Tugas", "Tambah ke Favorit", "Bagikan", "Hapus")

        AlertDialog.Builder(this)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editTask()
                    1 -> toggleFavorite()
                    2 -> shareTask()
                    3 -> deleteTask()
                }
            }
            .show()
    }

    private fun editTask() {
        val intent = Intent(this, EditTaskActivity::class.java).apply {
            putExtra("TASK_POSITION", taskPosition)
            putExtra("TASK_TITLE", tvTitle.text.toString())
            putExtra("TASK_DESCRIPTION", tvDescription.text.toString())
            putExtra("TASK_DATE", tvDate.text.toString())
            putExtra("TASK_IS_FAVORITE", isFavorite)
        }
        editTaskLauncher.launch(intent)
    }

    private fun toggleFavorite() {
        isFavorite = !isFavorite
        Toast.makeText(
            this,
            if (isFavorite) "Ditambahkan ke Favorit ⭐" else "Dihapus dari Favorit",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun shareTask() {
        val shareText = buildString {
            append("📝 ${tvTitle.text}\n\n")
            append("${tvDescription.text}\n")
            append("📅 ${tvDate.text}\n\n")
            cardList.forEachIndexed { i, card ->
                append("${i + 1}. $card\n")
            }
        }

        startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                },
                "Bagikan tugas melalui"
            )
        )
    }

    private fun deleteTask() {
        AlertDialog.Builder(this)
            .setTitle("Hapus Tugas")
            .setMessage("Yakin ingin menghapus tugas ini?")
            .setPositiveButton("Hapus") { _, _ ->
                val resultIntent = Intent().apply {
                    putExtra("ACTION", "DELETE")
                    putExtra("TASK_POSITION", taskPosition)
                }
                setResult(RESULT_OK, resultIntent)
                Toast.makeText(this, "Tugas berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun saveTask() {
        val resultIntent = Intent().apply {
            putExtra("ACTION", "UPDATE")
            putExtra("TASK_POSITION", taskPosition)
            putExtra("TASK_TITLE", tvTitle.text.toString())
            putExtra("TASK_DESCRIPTION", tvDescription.text.toString())
            putExtra("TASK_DATE", tvDate.text.toString())
            putExtra("TASK_IS_FAVORITE", isFavorite)
            putStringArrayListExtra("TASK_CARDS", ArrayList(cardList))
        }
        setResult(RESULT_OK, resultIntent)
        Toast.makeText(this, "Perubahan disimpan!", Toast.LENGTH_SHORT).show()
        finish()
    }
}