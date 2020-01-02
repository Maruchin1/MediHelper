package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.model.ProfileEditData
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException

interface GetProfileEditDataUseCase {

    @Throws(ProfileNotFoundException::class)
    suspend fun execute(profileId: String): ProfileEditData?
}