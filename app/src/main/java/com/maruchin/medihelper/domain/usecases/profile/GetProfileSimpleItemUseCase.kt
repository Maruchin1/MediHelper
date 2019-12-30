package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.model.ProfileSimpleItem

interface GetProfileSimpleItemUseCase {

    suspend fun execute(profileId: String): ProfileSimpleItem?
}