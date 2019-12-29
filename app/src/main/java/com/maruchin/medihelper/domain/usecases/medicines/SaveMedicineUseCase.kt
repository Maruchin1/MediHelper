package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineState
import com.maruchin.medihelper.domain.utils.MedicineValidator
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SaveMedicineUseCase(
    private val medicineRepo: MedicineRepo,
    private val validator: MedicineValidator
) {
    suspend fun execute(params: Params): MedicineValidator.Errors = withContext(Dispatchers.Default) {
        val validatorParams = MedicineValidator.Params(
            name = params.name,
            unit = params.unit,
            expireDate = params.expireDate,
            packageSize = params.packageSize,
            currState = params.currState
        )
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            saveMedicineToRepo(params)
        }
        return@withContext errors
    }

    private suspend fun saveMedicineToRepo(params: Params) {
        val medicine = Medicine(
            entityId = params.medicineId ?: "",
            name = params.name!!,
            unit = params.unit!!,
            expireDate = params.expireDate!!,
            state = MedicineState(
                packageSize = params.packageSize ?: 0f,
                currState = params.currState ?: 0f
            ),
            pictureName = params.pictureFile?.name ?: params.pictureName
        )
        if (params.pictureFile != null) {
            medicineRepo.saveMedicinePicture(params.pictureFile)
        }
        if (params.medicineId == null) {
            medicineRepo.addNew(medicine)
        } else {
            medicineRepo.update(medicine)
        }
    }


    data class Params(
        val medicineId: String?,
        val name: String?,
        val unit: String?,
        val expireDate: AppExpireDate?,
        val packageSize: Float?,
        val currState: Float?,
        val pictureName: String?,
        val pictureFile: File?
    )
}