package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineState
import com.maruchin.medihelper.domain.model.MedicineErrors
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.SaveMedicineUseCase
import com.maruchin.medihelper.domain.utils.MedicineValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveMedicineUseCaseImpl(
    private val medicineRepo: MedicineRepo,
    private val validator: MedicineValidator
) : SaveMedicineUseCase {

    override suspend fun execute(params: SaveMedicineUseCase.Params): MedicineErrors = withContext(Dispatchers.Default) {
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

    private suspend fun saveMedicineToRepo(params: SaveMedicineUseCase.Params) {
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
}