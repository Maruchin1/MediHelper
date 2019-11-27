package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.model.ProfileSimpleItem
import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetProfileSimpleItemUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(profileId: String): ProfileSimpleItem? {
        return profileRepo.getById(profileId)?.let { profile ->
            ProfileSimpleItem(profile)
        }
    }
}