package com.example.medihelper.remotedatabase.dto

import com.google.gson.annotations.SerializedName

data class PersonProfileDataDto(
    @SerializedName(value = "personName")
    val personName: String,

    @SerializedName(value = "personColorResId")
    val personColorResId: Int,

    @SerializedName(value = "authToken")
    val authToken: String
)