package com.maruchin.medihelper.presentation.feature.plans.daystype


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentDaysOfWeekBinding
import com.maruchin.medihelper.presentation.feature.plans.AddEditMedicinePlanViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DaysOfWeekFragment : Fragment() {
    private val TAG = DaysOfWeekFragment::class.simpleName

    private val viewModel: AddEditMedicinePlanViewModel by sharedViewModel(from = { parentFragment!! })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.daysOfWeek.observe(viewLifecycleOwner, Observer { daysOfWeek ->
            Log.d(TAG, "daysOfWeek change = $daysOfWeek")
        })
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentDaysOfWeekBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_days_of_week, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
