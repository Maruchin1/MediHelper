package com.example.medihelper.remotedatabase.pojos.medicineplan

import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.google.gson.annotations.SerializedName

data class MedicinePlanPostDto(
    @SerializedName(value = "medicinePlanLocalId")
    val medicinePlanLocalId: Int,

    @SerializedName(value = "medicineRemoteId")
    val medicineRemoteId: Long,

    @SerializedName(value = "personRemoteId")
    val personRemoteId: Long,

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

    @SerializedName(value = "timeOfTakingList")
    val timeOfTakingList: List<TimeOfTakingDto>
) {
    companion object {
        suspend fun fromMedicinePlanEntity(
            medicinePlanEntity: MedicinePlanEntity,
            medicineRepository: MedicineRepository,
            personRepository: PersonRepository
        ) = MedicinePlanPostDto(
            medicinePlanLocalId = medicinePlanEntity.medicinePlanID,
            medicineRemoteId = medicineRepository.getRemoteID(medicinePlanEntity.medicineID)!!,
            personRemoteId = personRepository.getRemoteID(medicinePlanEntity.personID)!!,
            startDate = medicinePlanEntity.startDate.jsonFormatString,
            endDate = medicinePlanEntity.endDate?.jsonFormatString,
            durationType = medicinePlanEntity.durationType.toString(),
            daysOfWeek = medicinePlanEntity.daysOfWeek,
            intervalOfDays = medicinePlanEntity.intervalOfDays,
            daysType = medicinePlanEntity.daysType.toString(),
            timeOfTakingList = medicinePlanEntity.timeOfTakingList.map { TimeOfTakingDto.fromTimeOfTakingEntity(it) }
        )
    }
}