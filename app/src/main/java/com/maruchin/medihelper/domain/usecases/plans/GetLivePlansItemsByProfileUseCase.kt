package com.maruchin.medihelper.domain.usecases.plans

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.model.PlanItem
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException

interface GetLivePlansItemsByProfileUseCase {

    @Throws(MedicineNotFoundException::class)
    suspend fun execute(profileId: String): LiveData<List<PlanItem>>
}