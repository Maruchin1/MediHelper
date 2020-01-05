package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.PlanDetails
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException
import com.maruchin.medihelper.domain.usecases.plans.GetPlanDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPlanDetailsUseCaseImpl(
    private val planRepo: PlanRepo,
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo
) : GetPlanDetailsUseCase {

    override suspend fun execute(medicinePlanId: String): PlanDetails = withContext(Dispatchers.Default) {
        val medicinePlan = getMedicinePlanId(medicinePlanId)
        val medicine = getMedicine(medicinePlan.medicineId)
        val profile = getProfile(medicinePlan.profileId)
        return@withContext PlanDetails(medicinePlan, medicine, profile)
    }

    private suspend fun getMedicinePlanId(medicinePlanId: String): Plan {
        return planRepo.getById(medicinePlanId) ?: throw MedicinePlanNotFoundException()
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }

    private suspend fun getProfile(profileId: String): Profile {
        return profileRepo.getById(profileId) ?: throw ProfileNotFoundException()
    }
}