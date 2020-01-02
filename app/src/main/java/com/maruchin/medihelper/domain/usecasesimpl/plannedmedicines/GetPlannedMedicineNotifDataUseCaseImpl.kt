package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.PlannedMedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetPlannedMedicineNotifDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPlannedMedicineNotifDataUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo
) : GetPlannedMedicineNotifDataUseCase {

    override suspend fun execute(
        plannedMedicineId: String
    ): PlannedMedicineNotifData = withContext(Dispatchers.Default) {
        val plannedMedicine = getPlannedMedicine(plannedMedicineId)
        val medicine = getMedicine(plannedMedicine.medicineId)
        val profile = getProfile(plannedMedicine.profileId)

        return@withContext PlannedMedicineNotifData(plannedMedicine, profile, medicine)
    }

    private suspend fun getPlannedMedicine(plannedMedicineId: String): PlannedMedicine {
        return plannedMedicineRepo.getById(plannedMedicineId) ?: throw PlannedMedicineNotFoundException()
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }

    private suspend fun getProfile(profileId: String): Profile {
        return profileRepo.getById(profileId) ?: throw ProfileNotFoundException()
    }
}