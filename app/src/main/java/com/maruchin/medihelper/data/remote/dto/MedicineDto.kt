package com.maruchin.medihelper.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MedicineDto(
    @SerializedName(value = "medicineLocalId")
    val medicineLocalId: Int?,

    @SerializedName(value = "medicineRemoteId")
    val medicineRemoteId: Long?,

    @SerializedName(value = "medicineName")
    val medicineName: String,

    @SerializedName(value = "medicineUnit")
    val medicineUnit: String,

    @SerializedName(value = "expireDate")
    val expireDate: String,

    @SerializedName(value = "packageSize")
    val packageSize: Float?,

    @SerializedName(value = "currState")
    val currState: Float?,

    @SerializedName(value = "additionalInfo")
    val additionalInfo: String?,

    @SerializedName(value = "image_name")
    val imageName: String?
)