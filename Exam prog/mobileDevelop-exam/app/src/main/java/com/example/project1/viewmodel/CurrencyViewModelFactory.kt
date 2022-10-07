package com.example.project1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project1.data.CurrencyRepository

class CurrencyViewModelFactory(private val currencyRepository: CurrencyRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java))
            return CurrencyViewModel(currencyRepository)  as T
        throw IllegalArgumentException("ViewModel not found!")
    }
}