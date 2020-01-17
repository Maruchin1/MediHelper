package com.maruchin.medihelper.domain.usecases.saved_types

import androidx.lifecycle.LiveData

interface GetLiveSavedTypesUseCase {

    suspend fun execute(): LiveData<List<String>>
}