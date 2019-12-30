package com.maruchin.medihelper.domain.usecases.medicines

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.model.MedicineItem

interface GetLiveAllMedicinesItemsUseCase {

    suspend fun execute(): LiveData<List<MedicineItem>>
}