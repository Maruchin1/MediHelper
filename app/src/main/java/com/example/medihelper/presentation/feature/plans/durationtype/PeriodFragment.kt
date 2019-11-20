package com.example.medihelper.presentation.feature.plans.durationtype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentPeriodBinding
import com.example.medihelper.presentation.dialogs.SelectDateDialog
import com.example.medihelper.presentation.feature.plans.AddEditMedicinePlanViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PeriodFragment : Fragment() {

    private val viewModel: AddEditMedicinePlanViewModel by sharedViewModel(from = { requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentPeriodBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_period, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun onClickSelectDate(view: View) {
        when (view.id) {
            R.id.etx_start_date -> {
                SelectDateDialog().apply {
                    defaultDate = viewModel.medicinePlanForm.value?.startDate
                    setDateSelectedListener { selectedDate ->
                        viewModel.medicinePlanForm.value?.startDate = selectedDate
                    }
                    viewModel.colorPrimaryId.value?.let { setColorPrimary(it) }
                }.show(requireParentFragment().childFragmentManager)
            }
            R.id.etx_end_date -> {
                SelectDateDialog().apply {
                    defaultDate = viewModel.medicinePlanForm.value?.endDate
                    setDateSelectedListener { selectedDate ->
                        viewModel.medicinePlanForm.value?.endDate = selectedDate
                    }
                    viewModel.colorPrimaryId.value?.let { setColorPrimary(it) }
                }.show(requireParentFragment().childFragmentManager)
            }
        }
    }
}
