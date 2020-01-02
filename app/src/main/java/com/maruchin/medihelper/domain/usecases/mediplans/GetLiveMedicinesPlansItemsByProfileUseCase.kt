package com.maruchin.medihelper.domain.usecases.mediplans

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.model.MedicinePlanItem
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GetLiveMedicinesPlansItemsByProfileUseCase {

    @Throws(MedicineNotFoundException::class)
    suspend fun execute(profileId: String): LiveData<List<MedicinePlanItem>>
}