package com.example.medihelper.mainapp.addmedicineplan.daystype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.medihelper.R
import com.example.medihelper.dialogs.SelectNumberDialog
import com.example.medihelper.databinding.FragmentIntervalOfDaysBinding
import com.example.medihelper.mainapp.addmedicineplan.AddMedicinePlanViewModel

class IntervalOfDaysFragment : Fragment() {
    private val TAG = IntervalOfDaysFragment::class.simpleName

    private lateinit var planViewModel: AddMedicinePlanViewModel

    fun onClickSelectInterval() {
        val dialog = SelectNumberDialog().apply {
            title = "Wybierz odstęp dni"
            iconResID = R.drawable.round_access_time_black_36
            defaultNumber = planViewModel.intervalOfDaysLive.value
            setNumberSelectedListener { number ->
                planViewModel.intervalOfDaysLive.value = number
            }
        }
        dialog.show(childFragmentManager, SelectNumberDialog.TAG)
    }

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

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentIntervalOfDaysBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_interval_of_days, container, false)
        binding.viewModel = planViewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
