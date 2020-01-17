package com.maruchin.medihelper.domain.usecasesimpl.settings

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.repositories.MedicineUnitRepo
import com.maruchin.medihelper.domain.usecases.settings.GetLiveSavedMedicineUnitsUseCase

class GetLiveSavedMedicineUnitsUseCaseImpl(
    private val medicineUnitRepo: MedicineUnitRepo
) : GetLiveSavedMedicineUnitsUseCase {
    override suspend fun execute(): LiveData<List<String>> {
        return medicineUnitRepo.getLiveAll()
    }
}