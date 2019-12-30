package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.model.ProfileEditData

interface GetProfileEditDataUseCase {

    suspend fun execute(profileId: String): ProfileEditData?
}