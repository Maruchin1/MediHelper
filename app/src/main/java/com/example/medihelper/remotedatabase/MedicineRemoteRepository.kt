package com.example.medihelper.remotedatabase

import com.example.medihelper.remotedatabase.pojos.MedicineGetDto
import com.example.medihelper.remotedatabase.pojos.MedicinePostDto
import com.example.medihelper.remotedatabase.pojos.PostResponseDto
import retrofit2.http.*

interface MedicineRemoteRepository {

    @GET("medicines")
    suspend fun getAllMedicines(
        @Header("Authorization") authToken: String
    ): List<MedicineGetDto>

    @POST("medicines")
    suspend fun insertNewMedicine(
        @Header("Authorization") authToken: String,
        @Body medicinePostDto: MedicinePostDto
    ): PostResponseDto


    @DELETE("medicines/{id}")
    suspend fun deleteMedicine(
        @Header("Authorization") authToken: String,
        @Path("id") medicineRemoteId: Long
    )

    @POST("medicines/overwrite")
    suspend fun overwriteMedicines(
        @Header("Authorization") authToken: String,
        @Body medicinePostDtoList: List<MedicinePostDto>
    ): List<PostResponseDto>
}