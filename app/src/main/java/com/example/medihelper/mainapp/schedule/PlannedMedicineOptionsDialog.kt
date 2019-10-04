package com.example.medihelper.mainapp.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.medihelper.R
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.DialogPlannedMedicineOptionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlannedMedicineOptionsDialog : BottomSheetDialogFragment() {
    val TAG = PlannedMedicineOptionsDialog::class.simpleName

    private val viewModel: PlannedMedicineOptionsViewModel by viewModel()
    private val args: PlannedMedicineOptionsDialogArgs by navArgs()
    private val directions by lazy { PlannedMedicineOptionsDialogDirections }

    fun onClickChangePlannedMedicineStatus() {
        viewModel.changePlannedMedicineStatus()
        dismiss()
    }

    fun onClickNavigateToMedicineDetails() = viewModel.medicineID?.let {
        findNavController().navigate(directions.toMedicineDetailsFragment(it))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel.setArgs(args)
        return bind<DialogPlannedMedicineOptionsBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.dialog_planned_medicine_options,
            viewModel = viewModel
        )
    }


}