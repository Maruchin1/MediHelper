package com.example.medihelper.remotedata.dto

import com.google.gson.annotations.SerializedName

data class PersonDto(
    @SerializedName(value = "personLocalId")
    val personLocalId: Int?,

    @SerializedName(value = "personRemoteId")
    val personRemoteId: Long?,

    @SerializedName(value = "personName")
    val personName: String,

    @SerializedName(value = "personColorResId")
    val personColorResId: Int,

    @SerializedName(value = "connectionKey")
    val connectionKey: String?
)