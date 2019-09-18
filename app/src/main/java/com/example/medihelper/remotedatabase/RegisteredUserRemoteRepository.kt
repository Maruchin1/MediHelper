package com.example.medihelper.remotedatabase

import com.example.medihelper.remotedatabase.pojos.UserCredentialsDto
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisteredUserRemoteRepository {

    @POST("login")
    suspend fun loginUser(@Body userCredentials: UserCredentialsDto): String

    @POST("register")
    suspend fun registerNewUser(@Body userCredentials: UserCredentialsDto)
}