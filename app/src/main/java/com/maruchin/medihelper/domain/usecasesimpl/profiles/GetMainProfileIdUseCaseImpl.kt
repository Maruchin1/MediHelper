package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.GetMainProfileIdUseCase

class GetMainProfileIdUseCaseImpl(
    private val profileRepo: ProfileRepo
) : GetMainProfileIdUseCase {

    override suspend fun execute(): String? {
        return profileRepo.getMainId()
    }
}