package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.repositories.MedicineRepo

class GetMedicineEditDataUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(medicineId: String): MedicineEditData? {
        return medicineRepo.getById(medicineId)?.let { medicine ->
            MedicineEditData(
                medicineId = medicine.medicineId,
                unit = medicine.unit,
                name = medicine.name,
                expireDate = medicine.expireDate,
                packageSize = medicine.packageSize,
                currState = medicine.currState,
                additionalInfo = medicine.additionalInfo
            )
        }
    }

    data class MedicineEditData(
        val medicineId: String,
        val name: String,
        val unit: String,
        val expireDate: AppExpireDate,
        val packageSize: Float?,
        val currState: Float?,
        val additionalInfo: String?
    )
}