package com.maruchin.medihelper.domain.usecases.plannedmedicines

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException

interface GetLivePlannedMedicinesItemsByDateUseCase {

    @Throws(MedicineNotFoundException::class)
    suspend fun execute(profileId: String, date: AppDate): LiveData<List<PlannedMedicineItem>>
}