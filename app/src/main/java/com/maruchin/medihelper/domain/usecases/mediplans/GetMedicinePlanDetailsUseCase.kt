package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.model.MedicinePlanDetails
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GetMedicinePlanDetailsUseCase {

    @Throws(
        MedicinePlanNotFoundException::class,
        MedicineNotFoundException::class,
        ProfileNotFoundException::class
    )
    suspend fun execute(medicinePlanId: String): MedicinePlanDetails
}