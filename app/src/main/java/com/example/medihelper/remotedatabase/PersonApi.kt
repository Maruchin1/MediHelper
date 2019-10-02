package com.example.medihelper.remotedatabase

import com.example.medihelper.remotedatabase.dto.PersonProfileDataDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PersonApi {

    @GET("persons/profile-data")
    suspend fun getAuthToken(@Query("connectionKey") connectionKey: String): PersonProfileDataDto
}