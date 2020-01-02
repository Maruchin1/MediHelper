package com.maruchin.medihelper.domain.usecasesimpl.mediplans

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.MedicinePlanDetails
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException
import com.maruchin.medihelper.domain.usecases.mediplans.GetMedicinePlanDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicinePlanDetailsUseCaseImpl(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo
) : GetMedicinePlanDetailsUseCase {

    override suspend fun execute(medicinePlanId: String): MedicinePlanDetails = withContext(Dispatchers.Default) {
        val medicinePlan = getMedicinePlanId(medicinePlanId)
        val medicine = getMedicine(medicinePlan.medicineId)
        val profile = getProfile(medicinePlan.profileId)
        return@withContext MedicinePlanDetails(medicinePlan, medicine, profile)
    }

    private suspend fun getMedicinePlanId(medicinePlanId: String): MedicinePlan {
        return medicinePlanRepo.getById(medicinePlanId) ?: throw MedicinePlanNotFoundException()
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }

    private suspend fun getProfile(profileId: String): Profile {
        return profileRepo.getById(profileId) ?: throw ProfileNotFoundException()
    }
}