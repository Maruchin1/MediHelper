package com.maruchin.medihelper.domain.usecases.mediplans

interface DeleteMedicinesPlansUseCase {

    suspend fun execute(medicinePlanIds: List<String>)
}