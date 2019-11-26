package com.maruchin.medihelper.data.remote.dto

import com.maruchin.medihelper.domain.entities.DaysOfWeek
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

    @SerializedName(value = "durationType")
    val durationType: String,

    @SerializedName(value = "startDate")
    val startDate: String,

    @SerializedName(value = "endDate")
    val endDate: String?,

    @SerializedName(value = "daysType")
    val daysType: String?,

    @SerializedName(value = "daysOfWeek")
    val daysOfWeek: DaysOfWeek?,

    @SerializedName(value = "intervalOfDays")
    val intervalOfDays: Int?,

    @SerializedName(value = "timeOfTakingDtoList")
    val timeDoseList: List<TimeDoseDto>
)