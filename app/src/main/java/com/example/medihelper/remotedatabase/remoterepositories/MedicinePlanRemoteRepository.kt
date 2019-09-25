package com.example.medihelper.remotedatabase.remoterepositories

import com.example.medihelper.remotedatabase.pojos.MedicinePlanDto
import com.example.medihelper.remotedatabase.pojos.medicineplan.MedicinePlanGetDto
import com.example.medihelper.remotedatabase.pojos.medicineplan.MedicinePlanPostDto
import com.example.medihelper.remotedatabase.pojos.PostResponseDto
import com.example.medihelper.remotedatabase.pojos.SyncRequestDto
import retrofit2.http.*

interface MedicinePlanRemoteRepository {

    @GET("medicines-plans")
    suspend fun getAllMedicinesPlans(@Header("Authorization") authToken: String): List<MedicinePlanGetDto>

    @POST("medicines-plans/overwrite")
    suspend fun overwriteMedicinesPlans(
        @Header("Authorization") authToken: String,
        @Body postDtoList: List<MedicinePlanPostDto>
    ): List<PostResponseDto>

    @PUT("medicines-plans/synchronize")
    suspend fun synchronizeMedicinesPlans(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<MedicinePlanDto>
    ): List<MedicinePlanDto>
}