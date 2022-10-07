package com.example.project1.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.project1.ActivityCallback
import com.example.project1.LocalDataSource.ExchangeHistory
import com.example.project1.databinding.FragmentCurrencyExchangeBinding
import com.example.project1.viewmodel.CurrencyViewModel
import com.example.project1.viewmodel.CurrencyViewModelFactory
import com.example.project1.viewmodel.RepositoryInitializer
import java.time.LocalDate
import kotlin.math.floor

private const val CURRENCY_NAME = "CurrencyName"

class CurrencyExchangeFragment : Fragment(){

    private var firstCurrencyName: String? = null
    private var firstCurrencyRate: Float = 1f
    private var secondCurrencyName: String? = null
    private var secondCurrencyRate: Float = 0f

    private lateinit var viewModelFactory: CurrencyViewModelFactory
    private lateinit var currencyViewModel: CurrencyViewModel

    private lateinit var binding: FragmentCurrencyExchangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            firstCurrencyName = it.getStringArrayList(CURRENCY_NAME)?.get(0)
            secondCurrencyName = it.getStringArrayList(CURRENCY_NAME)?.get(1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentCurrencyExchangeBinding.inflate(layoutInflater)
        binding.secondCurrency.text = secondCurrencyName
        binding.firstCurrency.text = firstCurrencyName

        viewModelFactory = CurrencyViewModelFactory(RepositoryInitializer.getRepository(requireContext()))
        currencyViewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrencyViewModel::class.java)

        currencyViewModel.getCurrenciesFromLocalDataSource()
        var liveData = currencyViewModel.getLocalLiveDataForCurrencyList()
        liveData.observe(requireActivity(), Observer {
            liveData.value?.let {
                for (currency in it) {
                    if (currency.name == firstCurrencyName)
                        firstCurrencyRate = currency.rate
                    else if (currency.name == secondCurrencyName)
                        secondCurrencyRate = currency.rate
                }
            }
        })

        binding.firstValue.setText("1.0")
        binding.secondValue.setText(calculate(1f, firstCurrencyRate!!, secondCurrencyRate!!).toString())

        binding.firstValue.addTextChangedListener {
            dynamicTextChange(binding.firstValue, binding.secondValue, false)
        }

        binding.secondValue.addTextChangedListener {
            dynamicTextChange(binding.secondValue, binding.firstValue, true)
        }

        binding.backButton.setOnClickListener {
            val activityCallback = requireActivity() as ActivityCallback
            activityCallback.showCurrencyListView()
        }

        binding.exchangeButton.setOnClickListener {
            saveExchange(binding.firstValue.text.toString().toFloat(), binding.secondValue.text.toString().toFloat())

            val activityCallback = requireActivity() as ActivityCallback
            activityCallback.showCurrencyListView()
        }

        return binding.root
    }

    private fun saveExchange(currency1amount: Float, currency2amount: Float){
        currencyViewModel.insertExchangeData(
            ExchangeHistory(
            currency1Name = firstCurrencyName!!,
            currency2Name = secondCurrencyName!!,
            currency1Count = floor(currency1amount * 100.0f) / 100.0f,
            currency2Count = floor(currency2amount * 100.0f) / 100.0f,
            date =  LocalDate.of(2022, 2, 1).toString()
        )
        )
    }

    private fun dynamicTextChange(editTextChanged: EditText, editText: EditText, isBackExchange: Boolean){
        if (editTextChanged.isFocused) {
            try {
                if (!isBackExchange)
                    editText.setText(calculate(editTextChanged.text.toString().toFloat(),
                        firstCurrencyRate!!, secondCurrencyRate!!).toString())
                else
                    editText.setText(calculate(editTextChanged.text.toString().toFloat(),
                        secondCurrencyRate!!, firstCurrencyRate!!).toString())

            } catch (e: Exception) {
                editText.setText("0.0")
            }
        }
    }

    private fun calculate(count: Float, firstCurrencyRate: Float, secondCurrencyRate: Float): Float {
        return floor(count / firstCurrencyRate * secondCurrencyRate * 100.0f) / 100.0f
    }

    companion object {
        @JvmStatic
        fun newInstance(
            firstCurrencyName: String, secondCurrencyName: String) =

            CurrencyExchangeFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(CURRENCY_NAME, arrayListOf(firstCurrencyName, secondCurrencyName))
                }
            }
    }
}