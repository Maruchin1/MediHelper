package com.example.medihelper.mainapp.addeditmedicineplan.durationtype


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.medihelper.R
import com.example.medihelper.dialogs.SelectDateDialog
import com.example.medihelper.databinding.FragmentScheduleTypeOnceBinding
import com.example.medihelper.mainapp.addeditmedicineplan.AddEditMedicinePlanViewModel


class OnceFragment : Fragment() {
    private val TAG = OnceFragment::class.simpleName

    private lateinit var planViewModel: AddEditMedicinePlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            planViewModel = ViewModelProviders.of(this).get(AddEditMedicinePlanViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    fun onClickSelectDate() {
        Log.d(TAG, "onClickSelectDate")
        val dialog = SelectDateDialog()
        dialog.defaultDate = planViewModel.startDateLive.value
        dialog.setDateSelectedListener { date ->
            planViewModel.startDateLive.value = date
        }
        dialog.show(childFragmentManager, dialog.TAG)
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentScheduleTypeOnceBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_type_once, container, false)
        binding.viewModel = planViewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
