package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.model.MedicinePlanDetails
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetMedicinePlanDetailsUseCase(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(medicinePlanId: String): MedicinePlanDetails? {
        val medicinePlan = medicinePlanRepo.getById(medicinePlanId)
        val medicine = medicinePlan?.let { medicineRepo.getById(it.medicineId) }
        val profile = medicinePlan?.let { profileRepo.getById(it.profileId) }
        return if (medicinePlan != null && medicine != null && profile != null) {
            MedicinePlanDetails(medicinePlan, medicine, profile)
        } else null
    }
}