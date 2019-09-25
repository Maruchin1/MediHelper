package com.example.medihelper.remotedatabase.remoterepositories

import com.example.medihelper.remotedatabase.pojos.MedicineDto
import com.example.medihelper.remotedatabase.pojos.medicine.MedicineGetDto
import com.example.medihelper.remotedatabase.pojos.medicine.MedicinePostDto
import com.example.medihelper.remotedatabase.pojos.PostResponseDto
import com.example.medihelper.remotedatabase.pojos.SyncRequestDto
import retrofit2.http.*

interface MedicineRemoteRepository {

    @GET("medicines")
    suspend fun getAllMedicines(@Header("Authorization") authToken: String): List<MedicineGetDto>

    @POST("medicines/overwrite")
    suspend fun overwriteMedicines(
        @Header("Authorization") authToken: String,
        @Body postDtoList: List<MedicinePostDto>
    ): List<PostResponseDto>

    @PUT("medicines/synchronize")
    suspend fun synchronizeMedicines(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<MedicineDto>
    ): List<MedicineDto>
}