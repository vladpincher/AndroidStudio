package com.example.project1.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project1.databinding.FragmentTrendBinding
import java.util.*


class TrendFragment : Fragment() {

    private lateinit var binding: FragmentTrendBinding

    companion object {
        fun newInstance() = TrendFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTrendBinding.inflate(layoutInflater)
        return binding.root
    }
}