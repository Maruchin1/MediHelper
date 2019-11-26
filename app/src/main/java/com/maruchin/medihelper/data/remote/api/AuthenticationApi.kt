package com.maruchin.medihelper.data.remote.api

import com.maruchin.medihelper.data.remote.dto.ConnectedPersonDto
import com.maruchin.medihelper.data.remote.dto.LoginInputDto
import com.maruchin.medihelper.data.remote.dto.LoginResponseDto
import com.maruchin.medihelper.data.remote.dto.RegisterInputDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {

    @POST("authentication/login")
    suspend fun loginUser(@Body loginInputDto: LoginInputDto): LoginResponseDto

    @GET("authentication/patron-connect")
    suspend fun patronConnect(@Query("connectionKey") connectionKey: String): ConnectedPersonDto

    @POST("authentication/register")
    suspend fun registerNewUser(@Body registerInputDto: RegisterInputDto)
}