package com.example.medihelper.remotedatabase.dto

import com.example.medihelper.AppTime
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.google.gson.annotations.SerializedName

data class TimeOfTakingDto(
    @SerializedName(value = "doseSize")
    val doseSize: Float,

    @SerializedName(value = "time")
    val time: String
) {
    fun toTimeOfTakingEntity() = MedicinePlanEntity.TimeOfTaking(
        doseSize = doseSize,
        time = AppTime(time)
    )

    companion object {
        fun fromTimeOfTakingEntity(timeOfTaking: MedicinePlanEntity.TimeOfTaking) =
            TimeOfTakingDto(
                doseSize = timeOfTaking.doseSize,
                time = timeOfTaking.time.jsonFormatString
            )
    }
}