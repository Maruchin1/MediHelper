package com.example.medihelper.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PlannedMedicineDto(
    @SerializedName(value = "plannedMedicineLocalId")
    val plannedMedicineLocalId: Int?,

    @SerializedName(value = "plannedMedicineRemoteId")
    val plannedMedicineRemoteId: Long?,

    @SerializedName(value = "medicinePlanRemoteId")
    val medicinePlanRemoteId: Long,

    @SerializedName(value = "plannedDate")
    val plannedDate: String,

    @SerializedName(value = "plannedTime")
    val plannedTime: String,

    @SerializedName(value = "plannedDoseSize")
    val plannedDoseSize: Float,

    @SerializedName(value = "statusOfTaking")
    val statusOfTaking: String
)