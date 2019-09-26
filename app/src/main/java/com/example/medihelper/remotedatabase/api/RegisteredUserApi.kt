package com.example.medihelper.remotedatabase.api

import com.example.medihelper.remotedatabase.dto.*
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.PUT

interface RegisteredUserApi {

    @PATCH("registered-users/password")
    suspend fun changeUserPassword(@Header("Authorization") authToken: String, @Body newPasswordDto: NewPasswordDto)

    @PUT("registered-users/synchronization/medicines")
    suspend fun synchronizeMedicines(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<MedicineDto>
    ): List<MedicineDto>

    @PUT("registered-users/synchronization/persons")
    suspend fun synchronizePersons(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<PersonDto>
    ): List<PersonDto>

    @PUT("registered-users/synchronization/medicines-plans")
    suspend fun synchronizeMedicinesPlans(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<MedicinePlanDto>
    ): List<MedicinePlanDto>

    @PUT("registered-users/synchronization/planned-medicines")
    suspend fun synchronizePlannedMedicines(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<PlannedMedicineDto>
    ): List<PlannedMedicineDto>
}