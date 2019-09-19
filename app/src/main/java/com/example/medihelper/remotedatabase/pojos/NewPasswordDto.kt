package com.example.medihelper.remotedatabase.pojos

import com.google.gson.annotations.SerializedName

data class NewPasswordDto(
    @SerializedName("value")
    val value: String
)