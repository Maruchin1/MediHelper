package com.example.medihelper.mainapp.schedule.scheduletype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.medihelper.R
import com.example.medihelper.SelectDateDialogFragment
import com.example.medihelper.databinding.FragmentScheduleTypePeriodBinding
import com.example.medihelper.mainapp.schedule.AddToScheduleViewModel

class ScheduleTypePeriodFragment : Fragment() {
    private val TAG = ScheduleTypePeriodFragment::class.simpleName

    private lateinit var viewModel: AddToScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddToScheduleViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    fun onClickSelectDate(view: View) {
        val dialog = SelectDateDialogFragment()
        when (view.id) {
            R.id.etx_start_date -> dialog.resultSelectedDateStringLive = viewModel.startDateStringLive
            R.id.etx_end_date -> dialog.resultSelectedDateStringLive = viewModel.endDateStringLive
        }
        dialog.show(childFragmentManager, SelectDateDialogFragment.TAG)
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentScheduleTypePeriodBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_type_period, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
