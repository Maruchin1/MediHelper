package com.maruchin.medihelper.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewPasswordDto(
    @SerializedName("value")
    val value: String
)