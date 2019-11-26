package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.repositories.MedicineRepo

class SaveMedicineUseCase(
    private val medicineRepo: MedicineRepo
) {

    suspend fun execute(params: Params): ValidationErrors {
        val validationResult = isValid(params)
        if (validationResult.noErrors) {
            saveMedicineToRepo(params)
        }
        return validationResult
    }

    private suspend fun saveMedicineToRepo(params: Params) {
        val medicine = Medicine(
            medicineId = params.medicineId ?: "",
            name = params.name!!,
            unit = params.unit!!,
            expireDate = params.expireDate!!,
            packageSize = params.packageSize,
            currState = params.currState,
            additionalInfo = params.additionalInfo
        )
        if (params.medicineId == null) {
            medicineRepo.addNew(medicine)
        } else {
            medicineRepo.update(medicine)
        }
    }

    private fun isValid(params: Params): ValidationErrors {
        val result = ValidationErrors()
        with(params) {
            if (name.isNullOrEmpty()) {
                result.emptyName = true
            }
            if (unit.isNullOrEmpty()) {
                result.emptyUnit = true
            }
            if (expireDate == null) {
                result.emptyExpireDate = true
            }
            if (packageSize != null && currState != null && currState > packageSize) {
                result.currStateBiggerThanPackageSize = true
            }
        }
        return result
    }

    data class Params(
        val medicineId: String?,
        val name: String?,
        val unit: String?,
        val expireDate: AppExpireDate?,
        val packageSize: Float?,
        val currState: Float?,
        val additionalInfo: String?
    )

    data class ValidationErrors(
        var emptyName: Boolean = false,
        var emptyUnit: Boolean = false,
        var emptyExpireDate: Boolean = false,
        var currStateBiggerThanPackageSize: Boolean = false
    ) {
        val noErrors: Boolean
            get() = arrayOf(
                emptyName,
                emptyUnit,
                emptyExpireDate,
                currStateBiggerThanPackageSize
            ).all { !it }
    }
}