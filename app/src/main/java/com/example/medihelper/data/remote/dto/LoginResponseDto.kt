package com.example.medihelper.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("authToken")
    val authToken: String,

    @SerializedName("dataAvailable")
    val isDataAvailable: Boolean
)