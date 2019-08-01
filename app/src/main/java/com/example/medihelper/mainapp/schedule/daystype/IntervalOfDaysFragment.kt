package com.example.medihelper.mainapp.schedule.daystype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.medihelper.R
import com.example.medihelper.SelectNumberDialog
import com.example.medihelper.databinding.FragmentIntervalOfDaysBinding
import com.example.medihelper.mainapp.schedule.AddScheduledMedicineViewModel

class IntervalOfDaysFragment : Fragment() {
    private val TAG = IntervalOfDaysFragment::class.simpleName

    private lateinit var viewModel: AddScheduledMedicineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddScheduledMedicineViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    fun onClickSelectInterval() {
        val dialog = SelectNumberDialog()
        dialog.defaultNumber = viewModel.intervalOfDaysLive.value
        dialog.setNumberSelectedListener { number ->
            viewModel.intervalOfDaysLive.value = number
        }
        dialog.show(childFragmentManager, SelectNumberDialog.TAG)
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
