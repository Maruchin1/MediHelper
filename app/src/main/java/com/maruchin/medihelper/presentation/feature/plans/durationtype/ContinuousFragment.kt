package com.maruchin.medihelper.presentation.feature.plans.durationtype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentContinuousBinding
import com.maruchin.medihelper.presentation.dialogs.SelectDateDialog
import com.maruchin.medihelper.presentation.feature.plans.AddEditMedicinePlanViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ContinuousFragment : Fragment() {

    private val viewModel: AddEditMedicinePlanViewModel by sharedViewModel(from = { parentFragment!! })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentContinuousBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_continuous, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun onClickSelectDate() = SelectDateDialog().apply {
        defaultDate = viewModel.startDate.value
        setDateSelectedListener { date ->
            viewModel.startDate.value = date
        }
        viewModel.colorPrimaryId.value?.let { setColorPrimary(it) }
    }.show(requireParentFragment().childFragmentManager)
}
