package com.example.medihelper.remotedatabase.pojos.plannedmedicine

import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.google.gson.annotations.SerializedName

data class PlannedMedicinePostDto(
    @SerializedName(value = "plannedMedicineLocalId")
    val plannedMedicineLocalId: Int,

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
    companion object {
        suspend fun fromPlannedMedicineEntity(
            plannedMedicineEntity: PlannedMedicineEntity,
            medicinePlanRepository: MedicinePlanRepository
        ) = PlannedMedicinePostDto(
            plannedMedicineLocalId = plannedMedicineEntity.plannedMedicineID,
            medicinePlanRemoteId = medicinePlanRepository.getRemoteID(plannedMedicineEntity.medicinePlanID)!!,
            plannedDate = plannedMedicineEntity.plannedDate.jsonFormatString,
            plannedTime = plannedMedicineEntity.plannedTime.jsonFormatString,
            plannedDoseSize = plannedMedicineEntity.plannedDoseSize,
            statusOfTaking = plannedMedicineEntity.statusOfTaking.toString()
        )
    }
}