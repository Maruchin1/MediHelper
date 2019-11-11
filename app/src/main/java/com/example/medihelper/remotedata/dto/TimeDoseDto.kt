package com.example.medihelper.remotedata.dto

import com.google.gson.annotations.SerializedName

data class TimeDoseDto(
    @SerializedName(value = "doseSize")
    val doseSize: Float,

    @SerializedName(value = "time")
    val time: String
)