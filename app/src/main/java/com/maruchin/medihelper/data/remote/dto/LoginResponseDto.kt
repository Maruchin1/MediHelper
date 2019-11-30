package com.maruchin.medihelper.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("userName")
    val userName: String,

    @SerializedName("authToken")
    val authToken: String,

    @SerializedName("dataAvailable")
    val isDataAvailable: Boolean
)