package com.maruchin.medihelper.presentation.feature.planned_medicine_options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogPlannedMedicineOptionsBinding
import com.maruchin.medihelper.presentation.dialogs.SelectTimeDialog
import com.maruchin.medihelper.presentation.feature.calendar.CalendarFragmentDirections
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import com.maruchin.medihelper.presentation.framework.beginDelayedFade
import kotlinx.android.synthetic.main.dialog_planned_medicine_options.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlannedMedicineOptionsDialog :
    BaseBottomDialog<DialogPlannedMedicineOptionsBinding>(R.layout.dialog_planned_medicine_options) {
    override val TAG: String
        get() = "PlannedMedicineOptionsDialog"
    override val viewModel: PlannedMedicineOptionsViewModel by viewModel()

    lateinit var plannedMedicineId: String

    fun onClickChangePlannedMedicineTaken() {
        viewModel.changePlannedMedicineTaken()
        dismiss()
    }

    fun onClickOpenMedicinePlan(medicinePlanId: String) {
        requireParentFragment().findNavController().navigate(
            CalendarFragmentDirections.toMedicinePlanDetailsFragment(
                medicinePlanId
            )
        )
        dismiss()
    }

    fun onClickChangeForLater() {
        SelectTimeDialog(
            defaultTime = viewModel.plannedTime
        ).apply {
            setColorPrimary(viewModel.colorPrimary.value)
            setOnTimeSelectedListener { newTime ->
                viewModel.changePlannedTime(newTime)
                this@PlannedMedicineOptionsDialog.dismiss()
            }
        }.show(childFragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initData(plannedMedicineId)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loadingInProgress.observe(viewLifecycleOwner, Observer {
            root_lay.beginDelayedFade()
        })
    }
}