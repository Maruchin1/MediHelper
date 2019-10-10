package com.example.medihelper.mainapp.medicineplan.durationtype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentContinuousBinding
import com.example.medihelper.dialogs.SelectDateDialog
import com.example.medihelper.mainapp.medicineplan.AddEditMedicinePlanViewModel
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
        defaultDate = viewModel.startDateLive.value
        setDateSelectedListener { date ->
            viewModel.startDateLive.value = date
        }
        viewModel.colorPrimaryLive.value?.let { setColorPrimary(it) }
    }.show(requireParentFragment().childFragmentManager)
}
