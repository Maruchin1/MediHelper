package com.example.medihelper.remotedatabase.dto

import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
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
) {
    suspend fun toEntity(medicinePlanRepository: MedicinePlanRepository) = PlannedMedicineEntity(
        plannedMedicineID = plannedMedicineLocalId ?: 0,
        plannedMedicineRemoteID = plannedMedicineRemoteId,
        medicinePlanID = medicinePlanRepository.getIDByRemoteID(medicinePlanRemoteId)!!,
        plannedDate = AppDate(plannedDate),
        plannedTime = AppTime(plannedTime),
        plannedDoseSize = plannedDoseSize,
        statusOfTaking = PlannedMedicineEntity.StatusOfTaking.valueOf(statusOfTaking)
    )

    companion object {
        suspend fun fromEntity(plannedMedicineEntity: PlannedMedicineEntity, medicinePlanRepository: MedicinePlanRepository) = PlannedMedicineDto(
            plannedMedicineLocalId = plannedMedicineEntity.plannedMedicineID,
            plannedMedicineRemoteId = plannedMedicineEntity.plannedMedicineRemoteID,
            medicinePlanRemoteId = medicinePlanRepository.getRemoteID(plannedMedicineEntity.medicinePlanID)!!,
            plannedDate = plannedMedicineEntity.plannedDate.jsonFormatString,
            plannedTime = plannedMedicineEntity.plannedTime.jsonFormatString,
            plannedDoseSize = plannedMedicineEntity.plannedDoseSize,
            statusOfTaking = plannedMedicineEntity.statusOfTaking.toString()
        )
    }
}