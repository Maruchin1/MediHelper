package com.example.medihelper.remotedatabase

import com.example.medihelper.remotedatabase.pojos.NewPasswordDto
import com.example.medihelper.remotedatabase.pojos.UserCredentialsDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface RegisteredUserRemoteRepository {

    @PUT("change-password")
    suspend fun changeUserPassword(@Header("Authorization") authToken: String, @Body newPassword: NewPasswordDto)

    @POST("login")
    suspend fun loginUser(@Body userCredentials: UserCredentialsDto): String

    @POST("register")
    suspend fun registerNewUser(@Body userCredentials: UserCredentialsDto)
}