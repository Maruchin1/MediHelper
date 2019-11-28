package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetProfileColorsUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(): List<String> {
        return profileRepo.getColorsList()
    }
}