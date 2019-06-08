package com.example.medihelper.mainapp.schedule.dosetimeOptions


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentDoseTimeEverydayBinding
import com.example.medihelper.mainapp.schedule.AddToScheduleViewModel


class DoseTimeEverydayFragment : Fragment() {

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
        val binding: FragmentDoseTimeEverydayBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dose_time_everyday,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun onStartDateSelected(date: String) {
        viewModel.startDateLive.value = date
    }

    private fun onEndDateSelected(date: String) {
        viewModel.endDateLive.value = date
    }
}
