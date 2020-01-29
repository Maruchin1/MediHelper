package com.maruchin.medihelper.domain.usecases.plans

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.model.PlanCalendarData

interface GetLivePlansCalendarDataForProfileUseCase {
    suspend fun execute(profileId: String): LiveData<List<PlanCalendarData>>
}