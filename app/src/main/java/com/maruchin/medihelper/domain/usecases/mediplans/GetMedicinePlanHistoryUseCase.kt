package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.model.HistoryItem

interface GetMedicinePlanHistoryUseCase {

    suspend fun execute(medicinePlanId: String): List<HistoryItem>
}