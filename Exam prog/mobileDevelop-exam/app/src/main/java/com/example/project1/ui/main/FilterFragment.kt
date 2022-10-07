package com.example.project1.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.project1.ActivityCallback
import com.example.project1.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterBinding
    private var periodButtons: MutableList<Button> = mutableListOf()

    companion object {
        fun newInstance() = FilterFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFilterBinding.inflate(layoutInflater)
        val activityCallback = requireActivity() as ActivityCallback
        var period = 0

        binding.applyButton.setOnClickListener {
            activityCallback.showHistoryView(period)
        }

        periodButtons.add(binding.period7Button)
        periodButtons.add(binding.period30Button)
        periodButtons.add(binding.periodAllButton)

        binding.period7Button.setOnClickListener {
            binding.period7Button.isEnabled = false
            enableButtons(binding.period7Button)
            period = 7
        }

        binding.period30Button.setOnClickListener {
            binding.period30Button.isEnabled = false
            enableButtons(binding.period30Button)
            period = 30
        }

        binding.periodAllButton.setOnClickListener {
            binding.periodAllButton.isEnabled = false
            enableButtons(binding.periodAllButton)
            period = 0
        }


        return binding.root
    }

    private fun enableButtons(enableButton: Button) {
        for (button in periodButtons)
            if (button != enableButton)
                button.isEnabled = true
    }
}