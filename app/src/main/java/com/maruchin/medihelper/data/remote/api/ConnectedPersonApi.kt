package com.maruchin.medihelper.data.remote.api

import com.maruchin.medihelper.data.remote.dto.MedicineDto
import com.maruchin.medihelper.data.remote.dto.MedicinePlanDto
import com.maruchin.medihelper.data.remote.dto.PlannedMedicineDto
import retrofit2.http.*

interface ConnectedPersonApi {

    @GET("connected-persons/data/medicines")
    suspend fun getMedicines(@Header("Authorization") authToken: String): List<MedicineDto>

    @GET("connected-persons/data/medicines-plans")
    suspend fun getMedicinesPlans(@Header("Authorization") authToken: String): List<MedicinePlanDto>

    @PUT("connected-persons/data/planned-medicines")
    suspend fun synchronizePlannedMedicines(
        @Header("Authorization") authToken: String,
        @Body updateDtoList: List<PlannedMedicineDto>
    ): List<PlannedMedicineDto>
}