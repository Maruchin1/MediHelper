package com.example.medihelper.remotedatabase

import retrofit2.http.GET
import retrofit2.http.Query

interface PersonApi {

    @GET("persons/auth-token")
    suspend fun getAuthToken(@Query("personTempKey") personTempKey: String): String
}