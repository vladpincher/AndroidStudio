package com.example.project1.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1.ActivityCallback
import com.example.project1.LocalDataSource.ExchangeHistory
import com.example.project1.databinding.FragmentHistoryBinding
import com.example.project1.viewmodel.CurrencyViewModel
import com.example.project1.viewmodel.CurrencyViewModelFactory
import com.example.project1.viewmodel.RepositoryInitializer
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private const val PERIOD = "0"
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var viewModelFactory: CurrencyViewModelFactory
    private lateinit var currencyViewModel: CurrencyViewModel
    private var period: Int = 0

    companion object {
        @JvmStatic
        fun newInstance(period: Int) = HistoryFragment().apply {
            arguments = Bundle().apply{
                putInt(PERIOD, period)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            period = it.getInt(PERIOD)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentHistoryBinding.inflate(layoutInflater)
        adapter = HistoryAdapter()
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter

        binding.filterButton.setOnClickListener {
            val activityCallback = requireActivity() as ActivityCallback
            activityCallback.showHistoryFilterView()
        }

        viewModelFactory = CurrencyViewModelFactory(RepositoryInitializer.getRepository(requireContext()))
        currencyViewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrencyViewModel::class.java)

        currencyViewModel.getExchangeData()

        val liveData = currencyViewModel.getExchangeHistoryLiveData()

        liveData.observe(requireActivity(), Observer {
            liveData.value?.let { history->
                if (period != 0) {
                    val resultList = mutableListOf<ExchangeHistory>()
                    for (h in history) {
                        val historyDate = LocalDate.parse(h.date)
                        val today = LocalDate.now()
                        if (ChronoUnit.DAYS.between(historyDate, today) <= period)
                            resultList.add(h)
                    }
                    adapter.set(resultList)
                }
                else
                    adapter.set(history)
            }
        })

        return binding.root
    }
}