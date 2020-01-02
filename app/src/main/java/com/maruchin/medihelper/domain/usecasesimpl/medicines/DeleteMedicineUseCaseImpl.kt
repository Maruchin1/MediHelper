package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.DeleteMedicineUseCase
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.mediplans.DeleteMedicinesPlansUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteMedicineUseCaseImpl(
    private val medicineRepo: MedicineRepo,
    private val medicinePlanRepo: MedicinePlanRepo,
    private val deleteMedicinesPlansUseCase: DeleteMedicinesPlansUseCase
) : DeleteMedicineUseCase {

    override suspend fun execute(medicineId: String) = withContext(Dispatchers.Default) {
        val medicine = getMedicine(medicineId)
        if (medicine.pictureName != null) {
            medicineRepo.deleteMedicinePicture(medicine.pictureName)
        }
        medicineRepo.deleteById(medicineId)
        deletePlansUsingMedicine(medicineId)
        return@withContext
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }

    private suspend fun deletePlansUsingMedicine(medicineId: String) {
        val plansIdsUsingMedicine = getPlansIdsUsingMedicine(medicineId)
        deleteMedicinesPlansUseCase.execute(plansIdsUsingMedicine)
    }

    private suspend fun getPlansIdsUsingMedicine(medicineId: String): List<String> {
        val plansUsingMedicine = medicinePlanRepo.getListByMedicine(medicineId)
        return plansUsingMedicine.map { it.entityId }
    }
}