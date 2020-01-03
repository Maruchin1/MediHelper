package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.model.ProfileSimpleItem
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException

interface GetProfileSimpleItemUseCase {

    @Throws(ProfileNotFoundException::class)
    suspend fun execute(profileId: String): ProfileSimpleItem
}