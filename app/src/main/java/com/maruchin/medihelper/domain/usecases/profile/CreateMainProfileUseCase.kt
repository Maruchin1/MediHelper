package com.maruchin.medihelper.domain.usecases.profile

interface CreateMainProfileUseCase {

    suspend fun execute(profileName: String)
}