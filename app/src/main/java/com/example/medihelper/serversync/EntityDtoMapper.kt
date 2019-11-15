package com.example.medihelper.serversync

import com.example.medihelper.localdata.AppSharedPref
import com.example.medihelper.localdata.dao.MedicineDao
import com.example.medihelper.localdata.dao.MedicinePlanDao
import com.example.medihelper.localdata.dao.PersonDao
import com.example.medihelper.localdata.entity.*
import com.example.medihelper.localdata.type.*
import com.example.medihelper.remotedata.dto.*

class EntityDtoMapper(
    private val medicineDao: MedicineDao,
    private val personDao: PersonDao,
    private val medicinePlanDao: MedicinePlanDao,
    private val appSharedPref: AppSharedPref
) {

    fun personDtoToEntity(personDto: PersonDto) = PersonEntity(
        personId = personDto.personLocalId ?: 0,
        personRemoteId = personDto.personRemoteId,
        personName = personDto.personName,
        personColorResId = personDto.personColorResId,
        connectionKey = personDto.connectionKey,
        synchronizedWithServer = true
    )

    fun personEntityToDto(personEntity: PersonEntity) = PersonDto(
        personLocalId = personEntity.personId,
        personRemoteId = personEntity.personRemoteId,
        personName = personEntity.personName,
        personColorResId = personEntity.personColorResId,
        connectionKey = null
    )

    fun medicineDtoToEntity(medicineDto: MedicineDto) = MedicineEntity(
        medicineId = medicineDto.medicineLocalId ?: 0,
        medicineRemoteId = medicineDto.medicineRemoteId,
        medicineName = medicineDto.medicineName,
        medicineUnit = medicineDto.medicineUnit,
        expireDate = medicineDto.expireDate?.let { AppExpireDate(it) },
        packageSize = medicineDto.packageSize,
        currState = medicineDto.currState,
        additionalInfo = medicineDto.additionalInfo,
        imageName = null,
        synchronizedWithServer = true
    )

    fun medicineEntityToDto(medicineEntity: MedicineEntity) = MedicineDto(
        medicineLocalId = medicineEntity.medicineId,
        medicineRemoteId = medicineEntity.medicineRemoteId,
        medicineName = medicineEntity.medicineName,
        medicineUnit = medicineEntity.medicineUnit,
        expireDate = medicineEntity.expireDate?.jsonFormatString,
        packageSize = medicineEntity.packageSize,
        currState = medicineEntity.currState,
        additionalInfo = medicineEntity.additionalInfo,
        image = null
    )

    suspend fun medicinePlanDtoToEntity(medicinePlanDto: MedicinePlanDto) = MedicinePlanEntity(
        medicinePlanId = medicinePlanDto.medicinePlanLocalId ?: 0,
        medicinePlanRemoteId = medicinePlanDto.medicinePlanRemoteId,
        medicineId = medicineDao.getIdByRemoteId(medicinePlanDto.medicineRemoteId)!!,
        personId = if (medicinePlanDto.personRemoteId != null) {
            personDao.getIdByRemoteId(medicinePlanDto.personRemoteId)!!
        } else {
            appSharedPref.getMainPersonId()!!
        },
        startDate = AppDate(medicinePlanDto.startDate),
        endDate = medicinePlanDto.endDate?.let { AppDate(it) },
        durationType = DurationType.valueOf(medicinePlanDto.durationType),
        daysOfWeek = medicinePlanDto.daysOfWeek,
        intervalOfDays = medicinePlanDto.intervalOfDays,
        daysType = DaysType.valueOf(medicinePlanDto.daysType)
    )

    suspend fun medicinePlanEntityToDto(medicinePlanEntity: MedicinePlanEntity, timeDoseDtoList: List<TimeDoseDto>) = MedicinePlanDto(
        medicinePlanLocalId = medicinePlanEntity.medicinePlanId,
        medicinePlanRemoteId = medicinePlanEntity.medicinePlanRemoteId,
        medicineRemoteId = medicineDao.getRemoteIdById(medicinePlanEntity.medicineId)!!,
        personRemoteId = personDao.getRemoteIdById(medicinePlanEntity.personId),
        startDate = medicinePlanEntity.startDate.jsonFormatString,
        endDate = medicinePlanEntity.endDate?.jsonFormatString,
        durationType = medicinePlanEntity.durationType.toString(),
        daysOfWeek = medicinePlanEntity.daysOfWeek,
        intervalOfDays = medicinePlanEntity.intervalOfDays,
        daysType = medicinePlanEntity.daysType.toString(),
        timeDoseList = timeDoseDtoList
    )

    suspend fun timeDoseDtoToEntity(timeDoseDto: TimeDoseDto, medicinePlanId: Int) = TimeDoseEntity(
        time = AppTime(timeDoseDto.time),
        doseSize = timeDoseDto.doseSize,
        medicinePlanId = medicinePlanId
    )

    suspend fun timeDoseEntityToDto(timeDoseEntity: TimeDoseEntity) = TimeDoseDto(
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
        statusOfTaking = StatusOfTaking.valueOf(plannedMedicineDto.statusOfTaking)
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