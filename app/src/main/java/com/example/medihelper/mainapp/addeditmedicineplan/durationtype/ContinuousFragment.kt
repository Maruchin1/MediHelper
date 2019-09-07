package com.example.medihelper.mainapp.addeditmedicineplan.durationtype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentContinuousBinding
import com.example.medihelper.dialogs.SelectDateDialog
import com.example.medihelper.mainapp.addeditmedicineplan.AddEditMedicinePlanViewModel

class ContinuousFragment : Fragment() {

    private val viewModel: AddEditMedicinePlanViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentContinuousBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_continuous, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun onClickSelectDate() {
        val dialog = SelectDateDialog()
        dialog.defaultDate = viewModel.startDateLive.value
        dialog.setDateSelectedListener { date ->
            viewModel.startDateLive.value = date
        }
        dialog.show(childFragmentManager, dialog.TAG)
    }
}
