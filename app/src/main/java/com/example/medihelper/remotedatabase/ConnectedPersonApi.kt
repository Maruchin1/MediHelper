package com.example.medihelper.remotedatabase

import com.example.medihelper.remotedatabase.dto.MedicineDto
import com.example.medihelper.remotedatabase.dto.MedicinePlanDto
import com.example.medihelper.remotedatabase.dto.PersonProfileDataDto
import com.example.medihelper.remotedatabase.dto.PlannedMedicineDto
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