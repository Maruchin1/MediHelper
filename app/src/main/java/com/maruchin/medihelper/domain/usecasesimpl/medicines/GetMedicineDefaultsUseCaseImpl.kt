package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.model.MedicineDefaults
import com.maruchin.medihelper.domain.repositories.MedicineTypeRepo
import com.maruchin.medihelper.domain.repositories.MedicineUnitRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDefaultsUseCase

class GetMedicineDefaultsUseCaseImpl(
    private val medicineUnitRepo: MedicineUnitRepo,
    private val medicineTypeRepo: MedicineTypeRepo
) : GetMedicineDefaultsUseCase {

    override suspend fun execute(): MedicineDefaults {
        val units = medicineUnitRepo.getAll()
        val types = medicineTypeRepo.getAll()
        return MedicineDefaults(
            medicineUnits = units,
            medicineTypes = types
        )
    }
}