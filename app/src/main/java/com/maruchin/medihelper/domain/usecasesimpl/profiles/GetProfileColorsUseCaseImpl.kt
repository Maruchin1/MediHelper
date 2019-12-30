package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorsUseCase

class GetProfileColorsUseCaseImpl(
    private val profileRepo: ProfileRepo
) : GetProfileColorsUseCase {

    override suspend fun execute(): List<String> {
        return profileRepo.getColorsList()
    }
}