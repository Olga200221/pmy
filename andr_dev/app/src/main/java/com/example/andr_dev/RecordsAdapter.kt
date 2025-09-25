package com.example.andr_dev

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.andr_dev.R

class RecordsAdapter : ListAdapter<ScoreWithPlayer, RecordsAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvScore: TextView = view.findViewById(R.id.tvScore)
        val tvDifficulty: TextView = view.findViewById(R.id.tvDifficulty)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvZodiac: TextView = view.findViewById(R.id.tvZodiac)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = getItem(position)
        holder.tvName.text = score.fullName
        holder.tvScore.text = "Очки: ${score.score}"
        holder.tvDifficulty.text = "Сложность: ${score.difficulty}"
        holder.tvDate.text = "Дата: ${score.date}"
        holder.tvZodiac.text = "Знак: ${score.zodiac}"
    }

    class DiffCallback : DiffUtil.ItemCallback<ScoreWithPlayer>() {
        override fun areItemsTheSame(oldItem: ScoreWithPlayer, newItem: ScoreWithPlayer): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ScoreWithPlayer, newItem: ScoreWithPlayer): Boolean = oldItem == newItem
    }
}