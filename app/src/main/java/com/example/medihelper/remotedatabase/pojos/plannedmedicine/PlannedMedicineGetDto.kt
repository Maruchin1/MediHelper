package com.example.medihelper.remotedatabase.pojos.plannedmedicine

import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.google.gson.annotations.SerializedName

data class PlannedMedicineGetDto(
    @SerializedName(value = "plannedMedicineRemoteId")
    val plannedMedicineRemoteId: Long,

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
    suspend fun toPlannedMedicineEntity(medicinePlanRepository: MedicinePlanRepository) = PlannedMedicineEntity(
        plannedMedicineRemoteID = plannedMedicineRemoteId,
        medicinePlanID = medicinePlanRepository.getIDByRemoteID(medicinePlanRemoteId),
        plannedDate = AppDate(plannedDate),
        plannedTime = AppTime(plannedTime),
        plannedDoseSize = plannedDoseSize,
        statusOfTaking = PlannedMedicineEntity.StatusOfTaking.valueOf(statusOfTaking)
    )
}