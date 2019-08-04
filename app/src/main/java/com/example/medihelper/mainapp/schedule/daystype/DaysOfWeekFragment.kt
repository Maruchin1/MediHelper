package com.example.medihelper.mainapp.schedule.daystype


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentDaysOfWeekBinding
import com.example.medihelper.mainapp.schedule.AddMedicinePlanViewModel

class DaysOfWeekFragment : Fragment() {
    private val TAG = DaysOfWeekFragment::class.simpleName

    private lateinit var planViewModel: AddMedicinePlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            planViewModel = ViewModelProviders.of(this).get(AddMedicinePlanViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        planViewModel.daysOfWeekLive.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "daysOfWeek change = $it")
        })
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentDaysOfWeekBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_days_of_week, container, false)
        binding.viewModel = planViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
