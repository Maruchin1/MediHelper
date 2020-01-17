package com.maruchin.medihelper.domain.usecases.saved_types

interface AddSavedTypeUseCase {

    suspend fun execute(type: String)
}