package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicineDetailsUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : GetMedicineDetailsUseCase {

    override suspend fun execute(medicineId: String): MedicineDetails = withContext(Dispatchers.Default) {
        val medicine = medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
        return@withContext MedicineDetails(medicine)
    }
}