package com.maruchin.medihelper.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterInputDto(
    @SerializedName("userName")
    val userName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)