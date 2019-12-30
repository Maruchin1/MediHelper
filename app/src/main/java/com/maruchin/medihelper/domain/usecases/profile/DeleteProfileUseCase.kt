package com.maruchin.medihelper.domain.usecases.profile


interface DeleteProfileUseCase {

    suspend fun execute(profileId: String)
}