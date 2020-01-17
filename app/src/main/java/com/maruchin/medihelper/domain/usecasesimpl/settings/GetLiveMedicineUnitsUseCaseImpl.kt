package com.maruchin.medihelper.domain.usecasesimpl.settings

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.repositories.MedicineUnitRepo
import com.maruchin.medihelper.domain.usecases.saved_types.GetLiveMedicineUnitsUseCase

class GetLiveMedicineUnitsUseCaseImpl(
    private val medicineUnitRepo: MedicineUnitRepo
) : GetLiveMedicineUnitsUseCase {
    override suspend fun execute(): LiveData<List<String>> {
        return medicineUnitRepo.getLiveAll()
    }
}