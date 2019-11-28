package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetMainProfileIdUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(): String? {
        return profileRepo.getMainId()
    }
}