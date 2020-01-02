package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorUseCase

class GetProfileColorUseCaseImpl(
    private val profileRepo: ProfileRepo
) : GetProfileColorUseCase {

    override suspend fun execute(profileId: String): String {
        val profile = getProfile(profileId)
        return profile.color
    }

    private suspend fun getProfile(profileId: String): Profile {
        return profileRepo.getById(profileId) ?: throw ProfileNotFoundException()
    }
}