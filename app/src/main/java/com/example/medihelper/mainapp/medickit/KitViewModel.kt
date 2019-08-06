package com.example.medihelper.mainapp.medickit

import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import java.io.File

class KitViewModel : ViewModel() {
    private val TAG = KitViewModel::class.simpleName

    val medicinesListLive = AppRepository.getMedicineListLive()
    val medicineTypesListLive = AppRepository.getMedicineTypeListLive()

    fun getMedicineDisplayData(medicine: Medicine): MedicineDisplayData {
        val medicineType = findMedicineTypeById(medicine.medicineTypeID)
        val medicineState = medicine.calcMedicineState()
        return MedicineDisplayData(
            medicineID = medicine.medicineID,
            medicineName = medicine.name,
            medicineTypeName = medicineType?.typeName ?: "--",
            stateAvailable = medicineState != null,
            medicineState = "${medicine.currState}/${medicine.packageSize}",
            stateLayoutWeight = medicineState,
            emptyLayoutWeight = medicineState?.let { 1 - it },
            stateColorId = medicineState?.let {
                when {
                    medicineState >= STATE_GOOD_LIMIT -> R.color.colorStateGood
                    medicineState > STATE_MEDIUM_LIMIT -> R.color.colorStateMedium
                    else -> R.color.colorStateSmall
                }
            },
            medicineImageFile = medicine.photoFilePath?.let { File(it) }
        )
    }

    private fun findMedicineTypeById(medicineTypeID: Int?) = medicineTypesListLive.value?.find { medicineType ->
        medicineType.medicineTypeID == medicineTypeID
    }

    companion object {
        const val STATE_GOOD_LIMIT = 0.75f
        const val STATE_MEDIUM_LIMIT = 0.4f
    }

    data class MedicineDisplayData(
        val medicineID: Int,
        val medicineName: String,
        val medicineTypeName: String,
        val stateAvailable: Boolean,
        val medicineState: String,
        val stateLayoutWeight: Float?,
        val emptyLayoutWeight: Float?,
        val stateColorId: Int?,
        val medicineImageFile: File?
    )
}