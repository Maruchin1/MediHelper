package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.model.MedicineEditData
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineEditDataUseCase
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicineEditDataUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : GetMedicineEditDataUseCase {

    override suspend fun execute(medicineId: String): MedicineEditData = withContext(Dispatchers.Default) {
        val medicine = getMedicine(medicineId)
        return@withContext MedicineEditData(medicine)
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }
}