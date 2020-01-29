package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.model.HistoryItem
import com.maruchin.medihelper.domain.usecases.plans.GetPlanHistoryUseCase

class GetPlanHistoryUseCaseImpl : GetPlanHistoryUseCase {

    override suspend fun execute(medicinePlanId: String): List<HistoryItem> {
        //todo change mock implementation
        return emptyList()
    }
}