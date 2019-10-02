package com.example.medihelper.remotedatabase

import com.example.medihelper.remotedatabase.dto.PersonProfileDataDto
import com.example.medihelper.remotedatabase.dto.UserCredentialsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {

    @GET("authentication/login")
    suspend fun loginUser(@Body userCredentialsDto: UserCredentialsDto): String

    @GET("authentication/patron-connect")
    suspend fun patronConnect(@Query("connectionKey") connectionKey: String): PersonProfileDataDto

    @POST("authentication/register")
    suspend fun registerNewUser(@Body userCredentialsDto: UserCredentialsDto)
}