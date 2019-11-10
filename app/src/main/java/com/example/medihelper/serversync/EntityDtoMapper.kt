package com.example.medihelper.serversync

import com.example.medihelper.custom.AppDate
import com.example.medihelper.custom.AppTime
import com.example.medihelper.localdatabase.dao.MedicineDao
import com.example.medihelper.localdatabase.dao.MedicinePlanDao
import com.example.medihelper.localdatabase.dao.PersonDao
import com.example.medihelper.localdatabase.entity.*
import com.example.medihelper.remotedatabase.dto.*

class EntityDtoMapper(
    private val medicineDao: MedicineDao,
    private val personDao: PersonDao,
    private val medicinePlanDao: MedicinePlanDao
) {

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

    suspend fun medicinePlanDtoToEntity(medicinePlanDto: MedicinePlanDto) = MedicinePlanEntity(
        medicinePlanID = medicinePlanDto.medicinePlanLocalId ?: 0,
        medicinePlanRemoteID = medicinePlanDto.medicinePlanRemoteId,
        medicineID = medicineDao.getIdByRemoteId(medicinePlanDto.medicineRemoteId)!!,
        personID = if (medicinePlanDto.personRemoteId != null) {
            personDao.getIdByRemoteId(medicinePlanDto.personRemoteId)!!
        } else {
            personDao.getMainPersonID()!!
        },
        startDate = AppDate(medicinePlanDto.startDate),
        endDate = medicinePlanDto.endDate?.let { AppDate(it) },
        durationType = MedicinePlanEntity.DurationType.valueOf(medicinePlanDto.durationType),
        daysOfWeek = medicinePlanDto.daysOfWeek,
        intervalOfDays = medicinePlanDto.intervalOfDays,
        daysType = MedicinePlanEntity.DaysType.valueOf(medicinePlanDto.daysType)
    )

    suspend fun medicinePlanEntityToDto(medicinePlanEntity: MedicinePlanEntity, timeDoseDtoList: List<TimeOfTakingDto>) = MedicinePlanDto(
        medicinePlanLocalId = medicinePlanEntity.medicinePlanID,
        medicinePlanRemoteId = medicinePlanEntity.medicinePlanRemoteID,
        medicineRemoteId = medicineDao.getRemoteIdById(medicinePlanEntity.medicineID)!!,
        personRemoteId = personDao.getRemoteIdById(medicinePlanEntity.personID),
        startDate = medicinePlanEntity.startDate.jsonFormatString,
        endDate = medicinePlanEntity.endDate?.jsonFormatString,
        durationType = medicinePlanEntity.durationType.toString(),
        daysOfWeek = medicinePlanEntity.daysOfWeek,
        intervalOfDays = medicinePlanEntity.intervalOfDays,
        daysType = medicinePlanEntity.daysType.toString(),
        timeOfTakingList = timeDoseDtoList
    )

    suspend fun timeDoseDtoToEntity(timeOfTakingDto: TimeOfTakingDto, medicinePlanId: Int) = TimeDoseEntity(
        time = AppTime(timeOfTakingDto.time),
        doseSize = timeOfTakingDto.doseSize,
        medicinePlanId = medicinePlanId
    )

    suspend fun timeDoseEntityToDto(timeDoseEntity: TimeDoseEntity) = TimeOfTakingDto(
        time = timeDoseEntity.time.jsonFormatString,
        doseSize = timeDoseEntity.doseSize
    )

    suspend fun plannedMedicineDtoToEntity(plannedMedicineDto: PlannedMedicineDto) = PlannedMedicineEntity(
        plannedMedicineId = plannedMedicineDto.plannedMedicineLocalId ?: 0,
        plannedMedicineRemoteId = plannedMedicineDto.plannedMedicineRemoteId,
        medicinePlanId = medicinePlanDao.getIdByRemoteId(plannedMedicineDto.medicinePlanRemoteId)!!,
        plannedDate = AppDate(plannedMedicineDto.plannedDate),
        plannedTime = AppTime(plannedMedicineDto.plannedTime),
        plannedDoseSize = plannedMedicineDto.plannedDoseSize,
        statusOfTaking = PlannedMedicineEntity.StatusOfTaking.valueOf(plannedMedicineDto.statusOfTaking)
    )

    suspend fun plannedMedicineEntityToDto(plannedMedicineEntity: PlannedMedicineEntity) = PlannedMedicineDto(
        plannedMedicineLocalId = plannedMedicineEntity.plannedMedicineId,
        plannedMedicineRemoteId = plannedMedicineEntity.plannedMedicineRemoteId,
        medicinePlanRemoteId = medicinePlanDao.getRemoteIdById(plannedMedicineEntity.medicinePlanId)!!,
        plannedDate = plannedMedicineEntity.plannedDate.jsonFormatString,
        plannedTime = plannedMedicineEntity.plannedTime.jsonFormatString,
        plannedDoseSize = plannedMedicineEntity.plannedDoseSize,
        statusOfTaking = plannedMedicineEntity.statusOfTaking.toString()
    )
}