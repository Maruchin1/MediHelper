package com.example.medihelper.mainapp.schedule.dosetimeOptions


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentDoseTimeSingleBinding
import com.example.medihelper.mainapp.schedule.AddToScheduleViewModel
import kotlinx.android.synthetic.main.fragment_dose_time_single.*


class DoseTimeSingleFragment : Fragment() {

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
            viewModel.showSelectDateDialogFragment(it, this::onDateSelected)
        }
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentDoseTimeSingleBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_dose_time_single, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun onDateSelected(date: String) {
        viewModel.startDateLive.value = date
    }
}
