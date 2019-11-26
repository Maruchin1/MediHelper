package com.maruchin.medihelper.presentation.feature.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogPlannedMedicineOptionsBinding
import com.maruchin.medihelper.presentation.framework.bind
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

    fun onClickNavigateToMedicineDetails() = viewModel.getMedicineId()?.let {
        findNavController().navigate(directions.toMedicineDetailsFragment(""))
    }

    fun onClickChangeForLater() {
        //todo dorobić zmianę przypomnienia
        dismiss()
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