package com.maruchin.medihelper.domain.usecases.mediplans

interface DeleteSingleMedicinePlanUseCase {

    suspend fun execute(medicinePlanId: String)
}