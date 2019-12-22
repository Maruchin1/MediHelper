package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.repositories.ProfileRepo

class DeleteProfileUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(profileId: String) {
        profileRepo.deleteById(profileId)
    }
}