package com.example.medihelper.serversync

import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import com.example.medihelper.localdatabase.entity.MedicineEntity
import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.example.medihelper.localdatabase.entity.PersonEntity
import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.remotedatabase.dto.*

class EntityDtoConverter(
    private val medicineRepository: MedicineRepository,
    private val personRepository: PersonRepository,
    private val medicinePlanRepository: MedicinePlanRepository
) {

    fun medicineDtoToEntity(medicineDto: MedicineDto) = MedicineEntity(
        medicineID = medicineDto.medicineLocalId ?: 0,
        medicineRemoteID = medicineDto.medicineRemoteId,
        medicineName = medicineDto.medicineName,
        medicineUnit = medicineDto.medicineUnit,
        expireDate = medicineDto.expireDate?.let { AppDate(it) },
        packageSize = medicineDto.packageSize,
        currState = medicineDto.currState,
        additionalInfo = medicineDto.additionalInfo,
        imageName = null,
        synchronizedWithServer = true
    )

    fun medicineEntityToDto(medicineEntity: MedicineEntity) = MedicineDto(
        medicineLocalId = medicineEntity.medicineID,
        medicineRemoteId = medicineEntity.medicineRemoteID,
        medicineName = medicineEntity.medicineName,
        medicineUnit = medicineEntity.medicineUnit,
        expireDate = medicineEntity.expireDate?.jsonFormatString,
        packageSize = medicineEntity.packageSize,
        currState = medicineEntity.currState,
        additionalInfo = medicineEntity.additionalInfo,
        image = null
    )

    fun personDtoToEntity(personDto: PersonDto) = PersonEntity(
        personID = personDto.personLocalId ?: 0,
        personRemoteID = personDto.personRemoteId,
        personName = personDto.personName,
        personColorResID = personDto.personColorResId,
        connectionKey = personDto.connectionKey,
        synchronizedWithServer = true
    )

    fun personEntityToDto(personEntity: PersonEntity) = PersonDto(
        personLocalId = personEntity.personID,
        personRemoteId = personEntity.personRemoteID,
        personName = personEntity.personName,
        personColorResId = personEntity.personColorResID,
        connectionKey = null
    )

    suspend fun medicinePlanDtoToEntity(medicinePlanDto: MedicinePlanDto) = MedicinePlanEntity(
        medicinePlanID = medicinePlanDto.medicinePlanLocalId ?: 0,
        medicinePlanRemoteID = medicinePlanDto.medicinePlanRemoteId,
        medicineID = medicineRepository.getLocalIDByRemoteID(medicinePlanDto.medicineRemoteId)!!,
        personID = if (medicinePlanDto.personRemoteId != null) {
            personRepository.getLocalIDByRemoteID(medicinePlanDto.personRemoteId)!!
        } else {
            personRepository.getMainPersonID()!!
        },
        startDate = AppDate(medicinePlanDto.startDate),
        endDate = medicinePlanDto.endDate?.let { AppDate(it) },
        durationType = MedicinePlanEntity.DurationType.valueOf(medicinePlanDto.durationType),
        daysOfWeek = medicinePlanDto.daysOfWeek,
        intervalOfDays = medicinePlanDto.intervalOfDays,
        daysType = MedicinePlanEntity.DaysType.valueOf(medicinePlanDto.daysType),
        timeOfTakingList = medicinePlanDto.timeOfTakingList.map {
            MedicinePlanEntity.TimeOfTaking(
                doseSize = it.doseSize,
                time = AppTime(it.time)
            )
        }
    )

    suspend fun medicinePlanEntityToDto(medicinePlanEntity: MedicinePlanEntity) = MedicinePlanDto(
        medicinePlanLocalId = medicinePlanEntity.medicinePlanID,
        medicinePlanRemoteId = medicinePlanEntity.medicinePlanRemoteID,
        medicineRemoteId = medicineRepository.getRemoteID(medicinePlanEntity.medicineID)!!,
        personRemoteId = personRepository.getRemoteID(medicinePlanEntity.personID),
        startDate = medicinePlanEntity.startDate.jsonFormatString,
        endDate = medicinePlanEntity.endDate?.jsonFormatString,
        durationType = medicinePlanEntity.durationType.toString(),
        daysOfWeek = medicinePlanEntity.daysOfWeek,
        intervalOfDays = medicinePlanEntity.intervalOfDays,
        daysType = medicinePlanEntity.daysType.toString(),
        timeOfTakingList = medicinePlanEntity.timeOfTakingList.map {
            TimeOfTakingDto(
                doseSize = it.doseSize,
                time = it.time.jsonFormatString
            )
        }
    )

    suspend fun plannedMedicineDtoToEntity(plannedMedicineDto: PlannedMedicineDto) = PlannedMedicineEntity(
        plannedMedicineID = plannedMedicineDto.plannedMedicineLocalId ?: 0,
        plannedMedicineRemoteID = plannedMedicineDto.plannedMedicineRemoteId,
        medicinePlanID = medicinePlanRepository.getLocalIDByRemoteID(plannedMedicineDto.medicinePlanRemoteId)!!,
        plannedDate = AppDate(plannedMedicineDto.plannedDate),
        plannedTime = AppTime(plannedMedicineDto.plannedTime),
        plannedDoseSize = plannedMedicineDto.plannedDoseSize,
        statusOfTaking = PlannedMedicineEntity.StatusOfTaking.valueOf(plannedMedicineDto.statusOfTaking)
    )

    suspend fun plannedMedicineEntityToDto(plannedMedicineEntity: PlannedMedicineEntity) = PlannedMedicineDto(
        plannedMedicineLocalId = plannedMedicineEntity.plannedMedicineID,
        plannedMedicineRemoteId = plannedMedicineEntity.plannedMedicineRemoteID,
        medicinePlanRemoteId = medicinePlanRepository.getRemoteID(plannedMedicineEntity.medicinePlanID)!!,
        plannedDate = plannedMedicineEntity.plannedDate.jsonFormatString,
        plannedTime = plannedMedicineEntity.plannedTime.jsonFormatString,
        plannedDoseSize = plannedMedicineEntity.plannedDoseSize,
        statusOfTaking = plannedMedicineEntity.statusOfTaking.toString()
    )
}