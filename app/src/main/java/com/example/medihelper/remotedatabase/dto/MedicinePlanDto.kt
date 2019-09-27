package com.example.medihelper.remotedatabase.dto

import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.google.gson.annotations.SerializedName

data class MedicinePlanDto(
    @SerializedName(value = "medicinePlanLocalId")
    val medicinePlanLocalId: Int?,

    @SerializedName(value = "medicinePlanRemoteId")
    val medicinePlanRemoteId: Long?,

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

    @SerializedName(value = "timeOfTakingDtoList")
    val timeOfTakingList: List<TimeOfTakingDto>
) {
    suspend fun toEntity(medicineRepository: MedicineRepository, personRepository: PersonRepository) = MedicinePlanEntity(
        medicinePlanID = medicinePlanLocalId ?: 0,
        medicinePlanRemoteID = medicinePlanRemoteId,
        medicineID = medicineRepository.getLocalIDByRemoteID(medicineRemoteId)!!,
        personID = personRepository.getLocalIDByRemoteID(personRemoteId)!!,
        startDate = AppDate(startDate),
        endDate = endDate?.let { AppDate(it) },
        durationType = MedicinePlanEntity.DurationType.valueOf(durationType),
        daysOfWeek = daysOfWeek,
        intervalOfDays = intervalOfDays,
        daysType = MedicinePlanEntity.DaysType.valueOf(daysType),
        timeOfTakingList = timeOfTakingList.map { it.toTimeOfTakingEntity() }
    )

    companion object {
        suspend fun fromEntity(
            medicinePlanEntity: MedicinePlanEntity,
            medicineRepository: MedicineRepository,
            personRepository: PersonRepository
        ) = MedicinePlanDto(
            medicinePlanLocalId = medicinePlanEntity.medicinePlanID,
            medicinePlanRemoteId = medicinePlanEntity.medicinePlanRemoteID,
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