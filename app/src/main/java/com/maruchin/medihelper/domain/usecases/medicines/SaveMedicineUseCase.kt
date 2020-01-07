package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.model.MedicineErrors
import java.io.File

interface SaveMedicineUseCase {

    suspend fun execute(params: Params): MedicineErrors

    data class Params(
        val medicineId: String?,
        val name: String?,
        val unit: String?,
        val type: String?,
        val expireDate: AppExpireDate?,
        val packageSize: Float?,
        val currState: Float?,
        val oldPictureName: String?,
        val newPictureFile: File?
    )
}