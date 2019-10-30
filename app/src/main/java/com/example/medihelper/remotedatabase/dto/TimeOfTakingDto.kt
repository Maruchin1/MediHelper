package com.example.medihelper.remotedatabase.dto

import com.google.gson.annotations.SerializedName

data class TimeOfTakingDto(
    @SerializedName(value = "doseSize")
    val doseSize: Float,

    @SerializedName(value = "time")
    val time: String
)