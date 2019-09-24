package com.example.medihelper.remotedatabase.remoterepositories

import com.example.medihelper.remotedatabase.pojos.medicineplan.MedicinePlanGetDto
import com.example.medihelper.remotedatabase.pojos.medicineplan.MedicinePlanPostDto
import com.example.medihelper.remotedatabase.pojos.PostResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MedicinePlanRemoteRepository {

    @GET("medicines-plans")
    suspend fun getAllMedicinesPlans(
        @Header("Authorization") authToken: String
    ): List<MedicinePlanGetDto>

    @POST("medicines-plans/overwrite")
    suspend fun overwriteMedicinesPlans(
        @Header("Authorization") authToken: String,
        @Body medicinePlanPostDtoList: List<MedicinePlanPostDto>
    ): List<PostResponseDto>
}