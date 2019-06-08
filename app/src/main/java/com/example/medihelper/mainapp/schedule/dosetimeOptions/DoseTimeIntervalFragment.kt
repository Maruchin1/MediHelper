package com.example.medihelper.mainapp.schedule.dosetimeOptions


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentDoseTimeIntervalBinding
import com.example.medihelper.mainapp.schedule.AddToScheduleViewModel
import kotlinx.android.synthetic.main.fragment_dose_time_interval.*


class DoseTimeIntervalFragment : Fragment() {

    private lateinit var viewModel: AddToScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddToScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupIntervalTypeSpinner()
    }

    fun onClickSelectDate(view: View) {
        context?.let {
            val onDateSelectedMethod = when (view.id) {
                R.id.txv_start -> this::onStartDateSelected
                else -> this::onEndDateSelected
            }
            viewModel.showSelectDateDialogFragment(it, onDateSelectedMethod)
        }
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentDoseTimeIntervalBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dose_time_interval,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupIntervalTypeSpinner() {
        context?.apply {
            ArrayAdapter.createFromResource(
                this,
                R.array.interval_types_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_interval_type.adapter = adapter
            }
        }
    }

    private fun onStartDateSelected(date: String) {
        viewModel.startDateLive.value = date
    }

    private fun onEndDateSelected(date: String) {
        viewModel.endDateLive.value = date
    }
}
