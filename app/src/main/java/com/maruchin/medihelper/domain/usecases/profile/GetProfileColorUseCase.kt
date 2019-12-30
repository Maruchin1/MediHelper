package com.maruchin.medihelper.domain.usecases.profile


interface GetProfileColorUseCase {

    suspend fun execute(profileId: String): String?
}