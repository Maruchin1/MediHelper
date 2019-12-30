package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.model.MedicineSimpleItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineSimpleItemUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicineSimpleItemUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : GetMedicineSimpleItemUseCase {

    override suspend fun execute(medicineId: String): MedicineSimpleItem? = withContext(Dispatchers.Default) {
        val medicine = medicineRepo.getById(medicineId)
        return@withContext medicine?.let { MedicineSimpleItem(it) }
    }
}