package com.example.project1.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.LocalDataSource.ExchangeHistory
import com.example.project1.databinding.HistoryItemBinding

class HistoryAdapter()
    : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    var historyList = mutableListOf<ExchangeHistory>()

    fun set(historyList: List<ExchangeHistory>) {
        this.historyList = historyList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HistoryItemBinding.inflate(inflater,parent, false)
        return HistoryAdapter.HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        historyList.getOrNull(position)?.let { history ->
            holder.binding.currency1Name.text = history.currency1Name
            holder.binding.currency2Name.text = history.currency2Name
            holder.binding.currency1amount.text = history.currency1Count.toString()
            holder.binding.currency2amount.text = history.currency2Count.toString()
            holder.binding.date.text = history.date
        }
    }

    override fun getItemCount(): Int {
        return historyList.count()
    }

    class HistoryViewHolder(var binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root)
}