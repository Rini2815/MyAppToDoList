package com.example.myapptodolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapptodolist.R
import com.example.myapptodolist.models.TaskCard

class TaskCardAdapter(
    private val cards: MutableList<TaskCard>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<TaskCardAdapter.CardViewHolder>() {

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbCard: CheckBox = view.findViewById(R.id.cbCard)
        val tvCardText: TextView = view.findViewById(R.id.tvCardText)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]

        holder.tvCardText.text = card.text
        holder.cbCard.isChecked = card.isDone

        // Hindari trigger ulang saat recycle
        holder.cbCard.setOnCheckedChangeListener(null)

        holder.cbCard.setOnCheckedChangeListener { _, isChecked ->
            card.isDone = isChecked
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int = cards.size

    // Helper untuk refresh data
    fun updateData(newCards: List<TaskCard>) {
        cards.clear()
        cards.addAll(newCards)
        notifyDataSetChanged()
    }
}
