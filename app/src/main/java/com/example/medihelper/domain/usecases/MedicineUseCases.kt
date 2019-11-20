package com.example.medihelper.domain.usecases

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.Medicine
import com.example.medihelper.domain.entities.MedicineInputData
import com.example.medihelper.domain.repositories.MedicineRepo

class MedicineUseCases(
    private val medicineRepo: MedicineRepo
) {

    suspend fun addNewMedicine(inputData: MedicineInputData) {
        val newMedicine = Medicine(
            medicineId = 0,
            name = inputData.name,
            unit = inputData.unit,
            expireDate = inputData.expireDate,
            packageSize = inputData.packageSize,
            currState = inputData.currState,
            additionalInfo = inputData.additionalInfo,
            image = inputData.image
        )
        medicineRepo.insert(newMedicine)
    }

    suspend fun updateMedicine(id: Int, inputData: MedicineInputData) {
        val existingMedicine = medicineRepo.getById(id)
        val updatedMedicine = existingMedicine.copy(
            name = inputData.name,
            unit = inputData.unit,
            expireDate = inputData.expireDate,
            packageSize = inputData.packageSize,
            additionalInfo = inputData.additionalInfo,
            image = inputData.image
        )
        medicineRepo.update(updatedMedicine)
    }

    suspend fun deleteMedicineById(id: Int) = medicineRepo.deleteById(id)

    suspend fun getMedicine(id: Int): Medicine = medicineRepo.getById(id)

    suspend fun reduceMedicineCurrState(id: Int, doseSize: Float) {
        val medicine = medicineRepo.getById(id)
        medicine.reduceCurrState(doseSize)
        medicineRepo.update(medicine)
    }

    fun getMedicineLiveById(id: Int): LiveData<Medicine> = medicineRepo.getLiveById(id)

    fun getAllMedicineListLive(): LiveData<List<Medicine>> = medicineRepo.getAllListLive()

    fun getMedicineListLiveFilteredByName(nameQuery: String) = medicineRepo.getListLiveFilteredByName(nameQuery)
}