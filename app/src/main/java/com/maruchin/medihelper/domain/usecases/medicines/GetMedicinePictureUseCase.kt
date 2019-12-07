package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo
import java.io.File

class GetMedicinePictureUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(pictureName: String): File {
        return medicineRepo.getMedicinePicture(pictureName)
    }
}