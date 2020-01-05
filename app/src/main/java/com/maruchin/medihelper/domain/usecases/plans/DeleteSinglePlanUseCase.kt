package com.maruchin.medihelper.domain.usecases.plans

interface DeleteSinglePlanUseCase {

    suspend fun execute(medicinePlanId: String)
}