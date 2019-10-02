package com.example.medihelper.remotedatabase

import com.example.medihelper.remotedatabase.dto.MedicineDto
import com.example.medihelper.remotedatabase.dto.MedicinePlanDto
import com.example.medihelper.remotedatabase.dto.PersonProfileDataDto
import com.example.medihelper.remotedatabase.dto.PlannedMedicineDto
import retrofit2.http.*

interface ConnectedPersonApi {

    @GET("connected-persons/data/medicines")
    fun getMedicines(@Header("Authorization") authToken: String): List<MedicineDto>

    @GET("connected-persons/data/medicines-plans")
    fun getMedicinesPlans(@Header("Authorization") authToken: String): List<MedicinePlanDto>

    @PUT("connected-persons/data/planned-medicines")
    fun synchronizePlannedMedicines(
        @Header("Authorization") authToken: String,
        @Body updateDtoList: List<PlannedMedicineDto>
    ): List<PlannedMedicineDto>
}