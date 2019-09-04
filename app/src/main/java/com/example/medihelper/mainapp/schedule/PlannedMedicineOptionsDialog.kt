package com.example.medihelper.mainapp.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.medihelper.R
import com.example.medihelper.databinding.DialogPlannedMedicineOptionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlannedMedicineOptionsDialog : BottomSheetDialogFragment() {
    val TAG = PlannedMedicineOptionsDialog::class.simpleName

    private val viewModel: PlannedMedicineOptionsViewModel by viewModels()
    private val args: PlannedMedicineOptionsDialogArgs by navArgs()

    fun onClickCloseDialog() = dismiss()

    fun onClickChangePlannedMedicineStatus() = viewModel.changePlannedMedicineStatus()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel.setArgs(args)
        val binding: DialogPlannedMedicineOptionsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_planned_medicine_options,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


}