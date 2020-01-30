package com.maruchin.medihelper.domain.usecasesimpl.saved_types

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.repositories.MedicineTypeRepo
import com.maruchin.medihelper.domain.usecases.saved_types.GetLiveMedicineTypesUseCase

class GetLiveMedicineTypesUseCaseImpl(
    private val medicineTypeRepo: MedicineTypeRepo
) : GetLiveMedicineTypesUseCase {

    override suspend fun execute(): LiveData<List<String>> {
        return medicineTypeRepo.getLiveAll()
    }
}