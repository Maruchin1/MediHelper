package com.example.medihelper.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medihelper.domain.deviceapi.CameraApi
import com.example.medihelper.domain.entities.Medicine
import com.example.medihelper.domain.entities.MedicineInputData
import com.example.medihelper.domain.repositories.MedicineRepo
import java.io.File

class MedicineUseCases(
    private val medicineRepo: MedicineRepo,
    private val cameraApi: CameraApi
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

    suspend fun reduceMedicineCurrState(id: Int, doseSize: Float) {
        val medicine = medicineRepo.getById(id)
        medicine.reduceCurrState(doseSize)
        medicineRepo.update(medicine)
    }

    suspend fun increaseMedicineCurrState(medicineId: Int, doseSize: Float) {
        val medicine = medicineRepo.getById(medicineId)
        medicine.increaseCurrState(doseSize)
        medicineRepo.update(medicine)
    }

    suspend fun deleteMedicineById(id: Int) = medicineRepo.deleteById(id)

    suspend fun getMedicineById(id: Int): Medicine = medicineRepo.getById(id)

    fun getMedicineLiveById(id: Int): LiveData<Medicine> = medicineRepo.getLiveById(id)

    fun getAllMedicineListLive(): LiveData<List<Medicine>> = medicineRepo.getAllListLive()

    fun getMedicineListLiveFilteredByName(nameQuery: String) = medicineRepo.getListLiveFilteredByName(nameQuery)

    fun getMedicineUnitList(): List<String> = medicineRepo.getUnitList()

    fun getMedicineUnitListLive(): LiveData<List<String>> = medicineRepo.getUnitListLive()

    fun saveMedicineUnitList(list: List<String>) = medicineRepo.saveUnitList(list)

    fun captureMedicinePhoto(capturedFileLive: MutableLiveData<File>) {
        return cameraApi.capturePhoto(capturedFileLive)
    }
}