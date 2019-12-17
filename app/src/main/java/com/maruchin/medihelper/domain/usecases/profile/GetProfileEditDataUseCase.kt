package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.model.ProfileEditData
import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetProfileEditDataUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(profileId: String): ProfileEditData? {
        val profile = profileRepo.getById(profileId)
        return profile?.let { ProfileEditData(it) }
    }
}