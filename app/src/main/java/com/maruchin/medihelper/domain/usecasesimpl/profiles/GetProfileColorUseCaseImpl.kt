package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorUseCase

class GetProfileColorUseCaseImpl(
    private val profileRepo: ProfileRepo
) : GetProfileColorUseCase {

    override suspend fun execute(profileId: String): String? {
        val profile = profileRepo.getById(profileId)
        return profile?.color
    }
}