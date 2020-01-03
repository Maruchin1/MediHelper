package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException


interface GetProfileColorUseCase {

    @Throws(ProfileNotFoundException::class)
    suspend fun execute(profileId: String): String
}