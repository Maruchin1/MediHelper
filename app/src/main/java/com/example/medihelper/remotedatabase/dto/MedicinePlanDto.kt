package com.example.medihelper.remotedatabase.dto

import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.google.gson.annotations.SerializedName

data class MedicinePlanDto(
    @SerializedName(value = "medicinePlanLocalId")
    val medicinePlanLocalId: Int?,

    @SerializedName(value = "medicinePlanRemoteId")
    val medicinePlanRemoteId: Long?,

    @SerializedName(value = "medicineRemoteId")
    val medicineRemoteId: Long,

    @SerializedName(value = "personRemoteId")
    val personRemoteId: Long?,

    @SerializedName(value = "startDate")
    val startDate: String,

    @SerializedName(value = "endDate")
    val endDate: String?,

    @SerializedName(value = "durationType")
    val durationType: String,

    @SerializedName(value = "daysOfWeek")
    val daysOfWeek: MedicinePlanEntity.DaysOfWeek?,

    @SerializedName(value = "intervalOfDays")
    val intervalOfDays: Int?,

    @SerializedName(value = "daysType")
    val daysType: String,

    @SerializedName(value = "timeOfTakingDtoList")
    val timeOfTakingList: List<TimeOfTakingDto>
)