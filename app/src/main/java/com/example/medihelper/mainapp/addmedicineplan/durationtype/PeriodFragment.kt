package com.example.medihelper.mainapp.addmedicineplan.durationtype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.medihelper.R
import com.example.medihelper.dialogs.SelectDateDialog
import com.example.medihelper.databinding.FragmentScheduleTypePeriodBinding
import com.example.medihelper.mainapp.addmedicineplan.AddMedicinePlanViewModel

class PeriodFragment : Fragment() {
    private val TAG = PeriodFragment::class.simpleName

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

    fun onClickSelectDate(view: View) {
        val selectedDateLive = when (view.id) {
            R.id.lay_start_date -> planViewModel.startDateLive
            R.id.lay_end_date -> planViewModel.endDateLive
            else -> null
        }
        val dialog = SelectDateDialog()
        selectedDateLive?.let { dialog.defaultDate = it.value }
        dialog.setDateSelectedListener { date ->
            selectedDateLive?.value = date
        }
        dialog.show(childFragmentManager, dialog.TAG)
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentScheduleTypePeriodBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_type_period, container, false)
        binding.viewModel = planViewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
