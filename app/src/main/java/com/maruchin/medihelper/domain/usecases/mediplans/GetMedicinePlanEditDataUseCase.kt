package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.model.MedicinePlanEditData
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GetMedicinePlanEditDataUseCase {

    @Throws(MedicinePlanNotFoundException::class)
    suspend fun execute(medicinePlanId: String): MedicinePlanEditData
}