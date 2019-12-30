package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.model.ProfileErrors

interface SaveProfileUseCase {

    suspend fun execute(params: Params): ProfileErrors

    data class Params(
        val profileId: String?,
        val name: String?,
        val color: String?
    )
}