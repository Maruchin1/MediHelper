package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.DeleteProfileUseCase

class DeleteProfileUseCaseImpl(
    private val profileRepo: ProfileRepo
) : DeleteProfileUseCase {

    override suspend fun execute(profileId: String) {
        profileRepo.deleteById(profileId)
    }
}