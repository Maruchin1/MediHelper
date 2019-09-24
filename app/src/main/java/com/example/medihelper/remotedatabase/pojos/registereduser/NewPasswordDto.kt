package com.example.medihelper.remotedatabase.pojos.registereduser

import com.google.gson.annotations.SerializedName

data class NewPasswordDto(
    @SerializedName("value")
    val value: String
)