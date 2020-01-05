package com.maruchin.medihelper.domain.usecases.plans

interface DeletePlansUseCase {

    suspend fun execute(medicinePlanIds: List<String>)
}