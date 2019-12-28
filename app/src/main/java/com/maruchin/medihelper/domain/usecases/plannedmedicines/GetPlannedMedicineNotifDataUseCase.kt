package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPlannedMedicineNotifDataUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(plannedMedicineId: String): PlannedMedicineNotifData = withContext(Dispatchers.Default) {
        val plannedMedicine = plannedMedicineRepo.getById(plannedMedicineId) ?: throw Exception("PlannedMedicine not found")
        val medicine = medicineRepo.getById(plannedMedicine.medicineId) ?: throw Exception("Medicine not found")
        val profile = profileRepo.getById(plannedMedicine.profileId) ?: throw Exception("Profile not found")

        return@withContext PlannedMedicineNotifData(plannedMedicine, profile, medicine)
    }
}