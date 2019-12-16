package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetProfileItemUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(profileId: String): ProfileItem? {
        return profileRepo.getById(profileId)?.let { profile ->
            ProfileItem(profile)
        }
    }
}