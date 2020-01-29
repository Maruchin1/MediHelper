package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.plans.DeletePlansUseCase
import com.maruchin.medihelper.domain.usecases.profile.DeleteProfileUseCase

class DeleteProfileUseCaseImpl(
    private val profileRepo: ProfileRepo,
    private val planRepo: PlanRepo,
    private val deletePlansUseCase: DeletePlansUseCase
) : DeleteProfileUseCase {

    override suspend fun execute(profileId: String) {
        profileRepo.deleteById(profileId)
        deletePlansUsingProfile(profileId)
    }

    private suspend fun deletePlansUsingProfile(profileId: String) {
        val plansIdsUsingProfile = getPlansIdsUsingProfile(profileId)
        deletePlansUseCase.execute(plansIdsUsingProfile)
    }

    private suspend fun getPlansIdsUsingProfile(profileId: String): List<String> {
        return planRepo.getListByProfile(profileId).map { it.entityId }
    }
}