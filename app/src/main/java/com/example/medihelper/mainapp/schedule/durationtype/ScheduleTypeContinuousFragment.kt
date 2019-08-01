package com.example.medihelper.mainapp.schedule.durationtype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.medihelper.AppDateTimeUtil

import com.example.medihelper.R
import com.example.medihelper.SelectDateDialog
import com.example.medihelper.databinding.FragmentScheduleTypeContinuousBinding
import com.example.medihelper.mainapp.schedule.AddScheduledMedicineViewModel

class ScheduleTypeContinuousFragment : Fragment() {
    private val TAG = ScheduleTypeContinuousFragment::class.simpleName

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

    fun onClickSelectDate() {
        val dialog = SelectDateDialog()
        dialog.defaultDate = viewModel.startDateLive.value
        dialog.setDateSelectedListener { date ->
            viewModel.startDateLive.value = date
        }
        dialog.show(childFragmentManager, SelectDateDialog.TAG)
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentScheduleTypeContinuousBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_type_continuous, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.appDateTimeUtil = AppDateTimeUtil
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
