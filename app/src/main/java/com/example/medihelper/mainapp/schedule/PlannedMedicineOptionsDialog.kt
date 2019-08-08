package com.example.medihelper.mainapp.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.R
import com.example.medihelper.databinding.DialogPlannedMedicineOptionsBinding
import com.example.medihelper.localdatabase.entities.PlannedMedicine
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlannedMedicineOptionsDialog : BottomSheetDialogFragment() {
    val TAG = PlannedMedicineOptionsDialog::class.simpleName

    var plannedMedicineId: Int? = null
    private lateinit var viewModel: PlannedMedicineOptionsViewModel

    fun onClickCloseDialog() = dismiss()

    fun onClickChangePlannedMedicineStatus() = viewModel.changePlannedMedicineStatus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlannedMedicineOptionsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setPlannedMedicineId(plannedMedicineId)
    }
}