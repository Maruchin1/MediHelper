package com.maruchin.medihelper.domain.usecases.settings

import androidx.lifecycle.LiveData

interface GetLiveSavedTypesUseCase {

    suspend fun execute(): LiveData<List<String>>
}