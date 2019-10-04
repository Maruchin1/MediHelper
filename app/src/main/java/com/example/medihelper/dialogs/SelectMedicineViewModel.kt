package com.example.medihelper.dialogs

import androidx.lifecycle.ViewModel
import com.example.medihelper.R
import com.example.medihelper.localdatabase.pojos.MedicineItem
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.mainapp.medicines.MedicinesViewModel
import java.io.File

class SelectMedicineViewModel(
    private val appFilesDir: File,
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    val medicineItemListLive = medicineRepository.getItemListLive()

    fun getMedicineDisplayData(medicineItem: MedicineItem): MedicineItemDisplayData {
        val medicineState = medicineItem.calcMedicineState()
        return MedicineItemDisplayData(
            medicineID = medicineItem.medicineID,
            medicineName = medicineItem.medicineName,
            stateAvailable = medicineState != null,
            stateLayoutWeight = medicineState,
            emptyLayoutWeight = medicineState?.let { 1 - it },
            stateColorId = medicineState?.let {
                when {
                    medicineState >= MedicinesViewModel.STATE_GOOD_LIMIT -> R.color.colorStateGood
                    medicineState > MedicinesViewModel.STATE_MEDIUM_LIMIT -> R.color.colorStateMedium
                    else -> R.color.colorStateSmall
                }
            },
            medicineImageFile = medicineItem.imageName?.let { File(appFilesDir, it) }
        )
    }

    data class MedicineItemDisplayData(
        val medicineID: Int,
        val medicineName: String,
        val stateAvailable: Boolean,
        val emptyLayoutWeight: Float?,
        val stateLayoutWeight: Float?,
        val stateColorId: Int?,
        val medicineImageFile: File?
    )
}