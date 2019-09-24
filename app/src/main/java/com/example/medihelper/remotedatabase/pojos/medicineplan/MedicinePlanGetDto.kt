package com.example.medihelper.remotedatabase.pojos.medicineplan

import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.google.gson.annotations.SerializedName

data class MedicinePlanGetDto(
    @SerializedName(value = "medicinePlanRemoteId")
    val medicinePlanRemoteId: Long,

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
    suspend fun toMedicinePlanEntity(medicineRepository: MedicineRepository, personRepository: PersonRepository): MedicinePlanEntity {
        val medicineID = medicineRepository.getIDByRemoteID(medicineRemoteId)
        val personID = personRepository.getIDByRemoteID(personRemoteId)
        return MedicinePlanEntity(
            medicinePlanRemoteID = medicinePlanRemoteId,
            medicineID = medicineID,
            personID = personID,
            startDate = AppDate(startDate),
            endDate = endDate?.let { AppDate(it) },
            durationType = MedicinePlanEntity.DurationType.valueOf(durationType),
            daysOfWeek = daysOfWeek,
            intervalOfDays = intervalOfDays,
            daysType = MedicinePlanEntity.DaysType.valueOf(daysType),
            timeOfTakingList = timeOfTakingList.map { it.toTimeOfTakingEntity() }
        )
    }
}