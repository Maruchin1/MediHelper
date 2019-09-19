package com.example.medihelper.mainapp.medicineplan.daystype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.medihelper.R
import com.example.medihelper.dialogs.SelectNumberDialog
import com.example.medihelper.databinding.FragmentIntervalOfDaysBinding
import com.example.medihelper.mainapp.medicineplan.AddEditMedicinePlanViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class IntervalOfDaysFragment : Fragment() {
    private val TAG = IntervalOfDaysFragment::class.simpleName

    private val viewModel: AddEditMedicinePlanViewModel by sharedViewModel(from = { parentFragment!! })

    fun onClickSelectInterval() {
        val dialog = SelectNumberDialog().apply {
            title = "Wybierz odstęp dni"
            iconResID = R.drawable.round_access_time_white_36
            defaultNumber = viewModel.intervalOfDaysLive.value
            setNumberSelectedListener { number ->
                viewModel.intervalOfDaysLive.value = number.toInt()
            }
        }
        dialog.show(childFragmentManager, SelectNumberDialog.TAG)
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
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
