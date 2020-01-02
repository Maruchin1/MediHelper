package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.model.MedicineSimpleItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineSimpleItemUseCase
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicineSimpleItemUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : GetMedicineSimpleItemUseCase {

    override suspend fun execute(medicineId: String): MedicineSimpleItem = withContext(Dispatchers.Default) {
        val medicine = getMedicine(medicineId)
        return@withContext MedicineSimpleItem(medicine)
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }
}