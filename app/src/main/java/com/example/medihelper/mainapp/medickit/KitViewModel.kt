package com.example.medihelper.mainapp.medickit

import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.localdatabase.pojos.MedicineItem
import java.io.File

class KitViewModel : ViewModel() {
    private val TAG = KitViewModel::class.simpleName

    val medicineKitItemListLive = AppRepository.getMedicineItemListLive()

    fun getMedicineKitItemDisplayData(medicineItem: MedicineItem): MedicineKitItemDisplayData {
        val medicineState = medicineItem.calcMedicineState()
        return MedicineKitItemDisplayData(
            medicineID = medicineItem.medicineID,
            medicineName = medicineItem.medicineName,
            medicineUnit = medicineItem.medicineUnit,
            stateAvailable = medicineState != null,
            medicineState = "${medicineItem.currState}/${medicineItem.packageSize}",
            stateLayoutWeight = medicineState,
            emptyLayoutWeight = medicineState?.let { 1 - it },
            stateColorId = medicineState?.let {
                when {
                    medicineState >= STATE_GOOD_LIMIT -> R.color.colorStateGood
                    medicineState > STATE_MEDIUM_LIMIT -> R.color.colorStateMedium
                    else -> R.color.colorStateSmall
                }
            },
            medicineImageFile = medicineItem.photoFilePath?.let { File(it) }
        )
    }

    companion object {
        const val STATE_GOOD_LIMIT = 0.75f
        const val STATE_MEDIUM_LIMIT = 0.4f
    }

    data class MedicineKitItemDisplayData(
        val medicineID: Int,
        val medicineName: String,
        val medicineUnit: String,
        val stateAvailable: Boolean,
        val medicineState: String,
        val stateLayoutWeight: Float?,
        val emptyLayoutWeight: Float?,
        val stateColorId: Int?,
        val medicineImageFile: File?
    )
}