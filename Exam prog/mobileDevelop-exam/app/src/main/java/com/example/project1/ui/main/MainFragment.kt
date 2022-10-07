package com.example.project1.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1.data.CurrencyData
import com.example.project1.databinding.MainFragmentBinding
import com.example.project1.viewmodel.CurrencyViewModel
import com.example.project1.viewmodel.CurrencyViewModelFactory
import com.example.project1.viewmodel.RepositoryInitializer
import androidx.lifecycle.Observer
import com.example.project1.ActivityCallback
import android.net.NetworkInfo

import android.net.ConnectivityManager


interface ItemClickListener{
    fun showCurrencyExchangeView(firstCurrency: CurrencyData)
    fun changeCurrencyState(currency: CurrencyData)
}

class MainFragment() : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: CurrencyAdapter
    private lateinit var viewModelFactory: CurrencyViewModelFactory
    private lateinit var currencyViewModel: CurrencyViewModel

    private var currencyList = mutableListOf<CurrencyData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater)

        val isOnline = isNetworkAvailable(requireContext())

        viewModelFactory = CurrencyViewModelFactory(RepositoryInitializer.getRepository(requireContext()))
        currencyViewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrencyViewModel::class.java)

        adapter = CurrencyAdapter(object : ItemClickListener{
            override fun showCurrencyExchangeView(firstCurrency: CurrencyData) {

                var secondCurrency = currencyList[0]

                if (!secondCurrency.favorite)
                    for (currency in currencyList)
                        if (currency.name == "RUB") {
                            secondCurrency = currency
                            break
                        }

                val activityCallback = requireActivity() as ActivityCallback
                activityCallback.showCurrencyExchangeView(firstCurrency, secondCurrency)
            }

            override fun changeCurrencyState(currency: CurrencyData){
                currencyViewModel.updateCurrency(currency)
                updateCurrencyList(currency)
            }
        })

        val layoutManager =  LinearLayoutManager(requireContext())
        binding.recycleview.layoutManager = layoutManager
        binding.recycleview.adapter = adapter

        if (isOnline)
            getRemoteCurrencyList()
        else
            getLocalCurrencyList()

        return binding.root
    }

    private fun updateCurrencyList(currency: CurrencyData) {
        for (curr in currencyList)
            if (curr.name == currency.name) {
                curr.favorite = currency.favorite
                break
            }
        updateAdapter()
    }

    private fun getLocalCurrencyList(){
        addLocalDataToList()
    }

    private fun addLocalDataToList() {
        val liveData = currencyViewModel.getLocalLiveDataForCurrencyList()
        currencyViewModel.getCurrenciesFromLocalDataSource()
        liveData.observe(requireActivity(), Observer {
            liveData.value?.let {  localCurrencyList->
                for (currency in localCurrencyList)
                    currencyList.add(CurrencyData(currency.name, currency.rate, currency.favorite))

                updateAdapter()
            }
        })
    }

    private fun getRemoteCurrencyList(): Boolean{
        var liveData = currencyViewModel.getData()
        currencyViewModel.getCurrencies()
        liveData.observe(requireActivity(), Observer {
            liveData.value?.let {
                 saveCurrencies(it.rates)
                showCurrencies(it.rates)
            }
        })
        return true
    }

    private fun saveCurrencies(currencies: Map<String, Float>) {
        val currencyListData = mutableListOf<CurrencyData>()
        val ratesOfCurrencies = currencies.values
        val namesOfCurrencies = currencies.keys

        for (j in 0 until currencies.size) {
            currencyListData.add(
                CurrencyData(
                name = namesOfCurrencies.elementAt(j),
                exchangeRate = ratesOfCurrencies.elementAt(j),
                favorite = false)
            )
        }

        val currencyViewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrencyViewModel::class.java)
        val liveData = currencyViewModel.getLocalLiveDataForCurrencyList()
        currencyViewModel.getCurrenciesFromLocalDataSource()

        liveData.observe(requireActivity(), Observer {
            if (it.isNotEmpty()) {
                currencyListData.clear()
                for (j in 0 until currencies.size) {
                    currencyListData.add(
                        CurrencyData(
                        name = namesOfCurrencies.elementAt(j),
                        exchangeRate = ratesOfCurrencies.elementAt(j),
                        favorite = it[j].favorite)
                    )

                    currencyViewModel.updateCurrency(currencyListData[j])
                }
            }
            else
                currencyViewModel.saveCurrencies(currencyListData)

            currencyList = currencyListData
            updateAdapter()
        })
    }

    fun showCurrencies(currencies: Map<String,Float>) {
        for (currency in currencies) {
            currencyList.add(CurrencyData(currency.key, currency.value, false))
        }
        updateAdapter()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false

        val info = connectivity.allNetworkInfo

        for (i in info.indices) {
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }

    fun updateAdapter(){
        currencyList.sortWith ( compareBy({!it.favorite},{it.name}))
        adapter.set(currencyList)
    }

}