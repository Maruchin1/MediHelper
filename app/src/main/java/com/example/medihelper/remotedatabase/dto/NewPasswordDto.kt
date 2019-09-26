package com.example.medihelper.remotedatabase.dto

import com.google.gson.annotations.SerializedName

data class NewPasswordDto(
    @SerializedName("value")
    val value: String
)