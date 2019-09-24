package com.example.medihelper.remotedatabase.remoterepositories

import com.example.medihelper.remotedatabase.pojos.PostResponseDto
import com.example.medihelper.remotedatabase.pojos.plannedmedicine.PlannedMedicineGetDto
import com.example.medihelper.remotedatabase.pojos.plannedmedicine.PlannedMedicinePostDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PlannedMedicineRemoteRepository {

    @GET("planned-medicines")
    suspend fun getAllPlannedMedicines(@Header("Authorization") authToken: String): List<PlannedMedicineGetDto>

    @POST("planned-medicines/overwrite")
    suspend fun overwritePlannedMedicines(
        @Header("Authorization") authToken: String,
        @Body postDtoList: List<PlannedMedicinePostDto>
    ): List<PostResponseDto>
}