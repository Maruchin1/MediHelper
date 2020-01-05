package com.maruchin.medihelper.domain.usecases.plans

import com.maruchin.medihelper.domain.model.HistoryItem

interface GetPlanHistoryUseCase {

    suspend fun execute(medicinePlanId: String): List<HistoryItem>
}