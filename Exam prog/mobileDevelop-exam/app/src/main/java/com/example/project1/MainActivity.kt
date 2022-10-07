package com.example.project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project1.data.CurrencyData
import com.example.project1.databinding.MainActivityBinding
import com.example.project1.ui.main.*

interface ActivityCallback{
    fun showCurrencyExchangeView(firstCurrency: CurrencyData, secondCurrency: CurrencyData)
    fun showCurrencyListView()
    fun showHistoryFilterView()
    fun showHistoryView(period: Int)
}

class MainActivity : AppCompatActivity(), ActivityCallback {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMenuClickListeners()

        if (savedInstanceState == null) {
            showCurrencyListView()
        }
    }

    private fun setMenuClickListeners() {

        binding.marketButton.setOnClickListener {
            showCurrencyListView()
        }

        binding.trendButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TrendFragment.newInstance())
                .commit()
        }

        binding.historyButton.setOnClickListener {
            showHistoryView(0)
        }
    }

    override fun showCurrencyExchangeView(firstCurrency: CurrencyData, secondCurrency: CurrencyData) {
        binding.menu.alpha = 0f
        binding.menu.isEnabled = false
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CurrencyExchangeFragment.newInstance(
                firstCurrency.name, secondCurrency.name))
            .commitNow()
    }

    override fun showCurrencyListView() {
        binding.menu.alpha = 1f
        binding.menu.isEnabled = true
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow()
    }

    override fun showHistoryFilterView() {
        binding.menu.alpha = 0f
        binding.menu.isEnabled = false
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FilterFragment.newInstance())
            .commit()
    }

    override fun showHistoryView(period: Int) {
        binding.menu.alpha = 1f
        binding.menu.isEnabled = true
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HistoryFragment.newInstance(period))
            .commit()
    }


}