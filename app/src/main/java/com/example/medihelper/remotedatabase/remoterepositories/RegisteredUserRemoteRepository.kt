package com.example.medihelper.remotedatabase.remoterepositories

import com.example.medihelper.remotedatabase.pojos.registereduser.NewPasswordDto
import com.example.medihelper.remotedatabase.pojos.registereduser.UserCredentialsDto
import retrofit2.http.*

interface RegisteredUserRemoteRepository {

    @PUT("registered-users/change-password")
    suspend fun changeUserPassword(@Header("Authorization") authToken: String, @Body newPassword: NewPasswordDto)

    @POST("registered-users/login")
    suspend fun loginUser(@Body userCredentials: UserCredentialsDto): String

    @POST("registered-users/register")
    suspend fun registerNewUser(@Body userCredentials: UserCredentialsDto)

    @GET("registered-users/has-data")
    suspend fun hasData(@Header("Authorization") authToken: String): Boolean

}