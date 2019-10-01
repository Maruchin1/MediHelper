package com.example.medihelper.remotedatabase

import com.example.medihelper.remotedatabase.dto.UserCredentialsDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("authentication/login")
    suspend fun loginUser(@Body userCredentialsDto: UserCredentialsDto): String

    @POST("authentication/register")
    suspend fun registerNewUser(@Body userCredentialsDto: UserCredentialsDto)
}