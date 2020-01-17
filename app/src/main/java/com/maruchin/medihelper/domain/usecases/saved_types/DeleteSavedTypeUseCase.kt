package com.maruchin.medihelper.domain.usecases.saved_types

interface DeleteSavedTypeUseCase {

    suspend fun execute(type: String)
}