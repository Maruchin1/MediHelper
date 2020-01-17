package com.maruchin.medihelper.domain.usecases.settings

interface DeleteSavedTypeUseCase {

    suspend fun execute(type: String)
}