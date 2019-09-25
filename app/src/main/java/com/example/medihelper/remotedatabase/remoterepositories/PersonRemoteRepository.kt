package com.example.medihelper.remotedatabase.remoterepositories

import com.example.medihelper.remotedatabase.pojos.PersonDto
import com.example.medihelper.remotedatabase.pojos.person.PersonGetDto
import com.example.medihelper.remotedatabase.pojos.person.PersonPostDto
import com.example.medihelper.remotedatabase.pojos.PostResponseDto
import com.example.medihelper.remotedatabase.pojos.SyncRequestDto
import retrofit2.http.*

interface PersonRemoteRepository  {

    @GET("persons")
    suspend fun getAllPersons(@Header("Authorization") authToken: String): List<PersonGetDto>

    @POST("persons/overwrite")
    suspend fun overwritePersons(
        @Header("Authorization") authToken: String,
        @Body postDtoList: List<PersonPostDto>
    ): List<PostResponseDto>

    @PUT("persons/synchronize")
    suspend fun synchronizePersons(
        @Header("Authorization") authToken: String,
        @Body syncRequestDto: SyncRequestDto<PersonDto>
    ): List<PersonDto>
}