package com.example.medihelper.remotedatabase.api

import com.example.medihelper.remotedatabase.dto.*
import retrofit2.http.*

interface RegisteredUserApi {

    @PATCH("registered-users/password")
    suspend fun changeUserPassword(
        @Header("Authorization") authToken: String,
        @Body newPasswordDto: NewPasswordDto
    )

    @PUT("registered-users/data/medicines")
    suspend fun synchronizeMedicines(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<MedicineDto>
    ): List<MedicineDto>

    @PUT("registered-users/data/persons")
    suspend fun synchronizePersons(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<PersonDto>
    ): List<PersonDto>

    @PUT("registered-users/data/medicines-plans")
    suspend fun synchronizeMedicinesPlans(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<MedicinePlanDto>
    ): List<MedicinePlanDto>

    @PUT("registered-users/data/planned-medicines")
    suspend fun synchronizePlannedMedicines(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<PlannedMedicineDto>
    ): List<PlannedMedicineDto>

    @DELETE("registered-users/data")
    suspend fun deleteAllData(@Header("Authorization") authToken: String)
}