package com.example.medihelper.remotedatabase

import com.example.medihelper.remotedatabase.pojos.PersonGetDto
import com.example.medihelper.remotedatabase.pojos.PersonPostDto
import com.example.medihelper.remotedatabase.pojos.PostResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PersonRemoteRepository  {

    @GET("persons")
    suspend fun getAllPersons(@Header("Authorization") authToken: String): List<PersonGetDto>

    @POST("persons/overwrite")
    suspend fun overwritePersons(
        @Header("Authorization") authToken: String,
        @Body personPostDtoList: List<PersonPostDto>
    ): List<PostResponseDto>
}