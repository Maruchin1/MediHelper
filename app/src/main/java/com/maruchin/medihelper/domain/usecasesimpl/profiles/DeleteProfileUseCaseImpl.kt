package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.mediplans.DeleteMedicinesPlansUseCase
import com.maruchin.medihelper.domain.usecases.profile.DeleteProfileUseCase

class DeleteProfileUseCaseImpl(
    private val profileRepo: ProfileRepo,
    private val medicinePlanRepo: MedicinePlanRepo,
    private val deleteMedicinesPlansUseCase: DeleteMedicinesPlansUseCase
) : DeleteProfileUseCase {

    override suspend fun execute(profileId: String) {
        profileRepo.deleteById(profileId)
        deletePlansUsingProfile(profileId)
    }

    private suspend fun deletePlansUsingProfile(profileId: String) {
        val plansIdsUsingProfile = getPlansIdsUsingProfile(profileId)
        deleteMedicinesPlansUseCase.execute(plansIdsUsingProfile)
    }

    private suspend fun getPlansIdsUsingProfile(profileId: String): List<String> {
        return medicinePlanRepo.getListByProfile(profileId).map { it.entityId }
    }
}