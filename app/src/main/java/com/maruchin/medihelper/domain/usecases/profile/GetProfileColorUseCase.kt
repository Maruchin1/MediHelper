package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetProfileColorUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(profileId: String): String? {
        val profile = profileRepo.getById(profileId)
        return profile?.color
    }
}