package com.maruchin.medihelper.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ConnectedPersonDto(
    @SerializedName(value = "personRemoteId")
    val personRemoteId: Long,

    @SerializedName(value = "personName")
    val personName: String,

    @SerializedName(value = "personColorResId")
    val personColorResId: Int,

    @SerializedName(value = "authToken")
    val authToken: String
) : Serializable