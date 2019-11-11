package com.example.medihelper.remotedata.api

import com.example.medihelper.remotedata.dto.MedicineDto
import com.example.medihelper.remotedata.dto.MedicinePlanDto
import com.example.medihelper.remotedata.dto.PlannedMedicineDto
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