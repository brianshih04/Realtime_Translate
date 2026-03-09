package com.example.translateapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.translateapp.data.TranslationHistory

class HistoryAdapter(
    private var histories: List<TranslationHistory>,
    private val onItemClickListener: ((TranslationHistory) -> Unit)? = null
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    
    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSourceText: TextView = itemView.findViewById(R.id.tvSourceText)
        val tvTargetText: TextView = itemView.findViewById(R.id.tvTargetText)
        val tvLanguages: TextView = itemView.findViewById(R.id.tvLanguages)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = histories[position]
        
        holder.tvSourceText.text = history.sourceText
        holder.tvTargetText.text = history.targetText
        holder.tvLanguages.text = "${history.sourceLanguage} → ${history.targetLanguage}"
        holder.tvTime.text = history.getFormattedTime()
        
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(history)
        }
    }
    
    override fun getItemCount(): Int = histories.size
    
    fun updateData(newData: List<TranslationHistory>) {
        histories = newData
        notifyDataSetChanged()
    }
}
