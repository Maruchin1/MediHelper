package com.example.medihelper.dialogs

import androidx.lifecycle.ViewModel
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.pojos.MedicineItem
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import java.io.File

class SelectMedicineViewModel(
    private val appFilesDir: File,
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    val medicineItemListLive = medicineRepository.getItemListLive()

    fun getMedicineDisplayData(medicineItem: MedicineItem): MedicineItemDisplayData {
        val medicineStateData = MedicineEntity.StateData(medicineItem.packageSize, medicineItem.currState)
        return MedicineItemDisplayData(
            medicineID = medicineItem.medicineID,
            medicineName = medicineItem.medicineName,
            stateAvailable = medicineStateData.stateAvailable,
            stateLayoutWeight = medicineStateData.stateWeight,
            emptyLayoutWeight = medicineStateData.emptyWeight,
            stateColorId = medicineStateData.colorId,
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