package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetMedicineDetailsUseCase(
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo
) {

    suspend fun execute(medicineId: String): MedicineDetails? {
        return medicineRepo.getById(medicineId)?.let { medicine ->
            val profileList = profileRepo.getListByMedicine(medicineId)
            return@let MedicineDetails(medicine, profileList)
        }
    }
}