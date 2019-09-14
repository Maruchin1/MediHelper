package com.example.medihelper.dialogs

import androidx.lifecycle.ViewModel
import com.example.medihelper.localdatabase.pojos.MedicineItem
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import java.io.File

class SelectMedicineViewModel(private val medicineRepository: MedicineRepository) : ViewModel() {

    val medicineItemListLive = medicineRepository.getItemListLive()

    fun getMedicineDisplayData(medicineItem: MedicineItem): MedicineItemDisplayData {
        return MedicineItemDisplayData(
            medicineID = medicineItem.medicineID,
            medicineName = medicineItem.medicineName,
            medicineState = "${medicineItem.currState}/${medicineItem.packageSize} ${medicineItem.medicineUnit}",
            medicineImageFile = medicineItem.photoFilePath?.let { File(it) }
        )
    }

    data class MedicineItemDisplayData(
        val medicineID: Int,
        val medicineName: String,
        val medicineState: String,
        val medicineImageFile: File?
    )
}