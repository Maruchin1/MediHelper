package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineState
import com.maruchin.medihelper.domain.model.MedicineErrors
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.SaveMedicineUseCase
import com.maruchin.medihelper.domain.utils.MedicineValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SaveMedicineUseCaseImpl(
    private val medicineRepo: MedicineRepo,
    private val validator: MedicineValidator
) : SaveMedicineUseCase {

    override suspend fun execute(params: SaveMedicineUseCase.Params): MedicineErrors =
        withContext(Dispatchers.Default) {
            val validatorParams = getValidatorParams(params)
            val errors = validator.validate(validatorParams)
            if (errors.noErrors) {
                saveMedicineToRepo(params)
            }
            return@withContext errors
        }

    private fun getValidatorParams(params: SaveMedicineUseCase.Params): MedicineValidator.Params {
        return MedicineValidator.Params(
            name = params.name,
            unit = params.unit,
            expireDate = params.expireDate,
            packageSize = params.packageSize,
            currState = params.currState
        )
    }

    private suspend fun saveMedicineToRepo(params: SaveMedicineUseCase.Params) {
        val actualPictureName: String? = when {
            pictureChanged(params) -> {
                medicineRepo.deleteMedicinePicture(params.oldPictureName!!)
                medicineRepo.saveMedicinePicture(params.newPictureFile!!)
                params.newPictureFile.name
            }
            firstPicture(params) -> {
                medicineRepo.saveMedicinePicture(params.newPictureFile!!)
                params.newPictureFile.name
            }
            pictureNotChanged(params) -> {
                params.oldPictureName
            }
            else -> null
        }
        val medicine = getMedicine(params, actualPictureName)
        if (params.medicineId == null) {
            medicineRepo.addNew(medicine)
        } else {
            medicineRepo.update(medicine)
        }
    }

    private fun pictureChanged(params: SaveMedicineUseCase.Params): Boolean {
        return params.newPictureFile != null && params.oldPictureName != null
    }

    private fun firstPicture(params: SaveMedicineUseCase.Params): Boolean {
        return params.newPictureFile != null && params.oldPictureName == null
    }

    private fun pictureNotChanged(params: SaveMedicineUseCase.Params): Boolean {
        return params.newPictureFile == null && params.oldPictureName != null
    }

    private fun getMedicine(params: SaveMedicineUseCase.Params, actualPictureName: String?): Medicine {
        return Medicine(
            entityId = params.medicineId ?: "",
            name = params.name!!,
            unit = params.unit!!,
            expireDate = params.expireDate!!,
            state = MedicineState(
                packageSize = params.packageSize ?: 0f,
                currState = params.currState ?: 0f
            ),
            pictureName = actualPictureName
        )
    }
}