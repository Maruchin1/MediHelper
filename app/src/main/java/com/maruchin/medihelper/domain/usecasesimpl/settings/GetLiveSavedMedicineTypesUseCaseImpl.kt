package com.maruchin.medihelper.domain.usecasesimpl.settings

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.repositories.MedicineTypeRepo
import com.maruchin.medihelper.domain.usecases.settings.GetLiveSavedMedicineTypesUseCase

class GetLiveSavedMedicineTypesUseCaseImpl(
    private val medicineTypeRepo: MedicineTypeRepo
) : GetLiveSavedMedicineTypesUseCase {

    override suspend fun execute(): LiveData<List<String>> {
        return medicineTypeRepo.getLiveAll()
    }
}