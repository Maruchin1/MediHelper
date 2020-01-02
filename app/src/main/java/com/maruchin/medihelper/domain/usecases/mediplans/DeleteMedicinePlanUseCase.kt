package com.maruchin.medihelper.domain.usecases.mediplans

interface DeleteMedicinePlanUseCase {

    suspend fun execute(medicinePlanId: String)
}