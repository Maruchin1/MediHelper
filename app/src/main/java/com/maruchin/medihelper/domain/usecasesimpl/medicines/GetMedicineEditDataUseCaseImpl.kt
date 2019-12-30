package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.model.MedicineEditData
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineEditDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicineEditDataUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : GetMedicineEditDataUseCase {

    override suspend fun execute(medicineId: String): MedicineEditData? = withContext(Dispatchers.Default) {
        return@withContext medicineRepo.getById(medicineId)?.let { medicine ->
            MedicineEditData(medicine)
        }
    }
}