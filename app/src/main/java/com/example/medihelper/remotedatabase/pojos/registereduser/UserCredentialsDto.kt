package com.example.medihelper.remotedatabase.pojos.registereduser

import com.google.gson.annotations.SerializedName

data class UserCredentialsDto(

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)