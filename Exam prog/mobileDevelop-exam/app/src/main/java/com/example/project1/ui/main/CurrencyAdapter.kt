package com.example.project1.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.data.CurrencyData
import com.example.project1.databinding.ItemBinding


class CurrencyAdapter(private val itemClickListener: ItemClickListener)
    : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    var currencies = mutableListOf<CurrencyData>()

    fun set(currencies: List<CurrencyData>){
        this.currencies = currencies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater,parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        currencies.getOrNull(position)?.let{
            var currency = it
            holder.binding.firstCurrencyName.text = currency.name

            if (currency.favorite)
                holder.binding.favorite.setImageResource(R.drawable.ic_star2)

            holder.binding.favorite.setOnClickListener {
                if (!currency.favorite) {
                    holder.binding.favorite.setImageResource(R.drawable.ic_star2)
                    currency.favorite = true
                }
                else{
                    holder.binding.favorite.setImageResource(R.drawable.ic_star)
                    currency.favorite = false
                }

                itemClickListener.changeCurrencyState(currency)
            }

            holder.binding.item.setOnClickListener {
                itemClickListener.showCurrencyExchangeView(currency)
            }
        }
    }

    override fun getItemCount(): Int {
        return currencies.count()
    }

    class CurrencyViewHolder(var binding: ItemBinding): RecyclerView.ViewHolder(binding.root)
}