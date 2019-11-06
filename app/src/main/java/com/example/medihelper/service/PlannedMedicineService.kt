package com.example.medihelper.service

import androidx.lifecycle.LiveData
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.DeletedHistory
import com.example.medihelper.localdatabase.dao.MedicinePlanDao
import com.example.medihelper.localdatabase.dao.PlannedMedicineDao
import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojo.MedicinePlanEditData
import com.example.medihelper.localdatabase.pojo.PlannedMedicineAlarmData
import com.example.medihelper.localdatabase.pojo.PlannedMedicineDetails
import com.example.medihelper.localdatabase.pojo.PlannedMedicineItem

interface PlannedMedicineService {
    suspend fun insert(entity: PlannedMedicineEntity): Long
    suspend fun insert(entityList: List<PlannedMedicineEntity>)
    suspend fun insertForMedicinePlan(medicinePlanId: Int)
    suspend fun update(entity: PlannedMedicineEntity)
    suspend fun update(entityList: List<PlannedMedicineEntity>)
    suspend fun updateForMedicinePlan(medicinePlanId: Int)
    suspend fun updateAllStatus()
    suspend fun delete(idList: List<Int>)
    suspend fun deleteFromDateByMedicinePlanID(date: AppDate, id: Int)
    suspend fun getEntity(id: Int): PlannedMedicineEntity
    suspend fun getEntityList(): List<PlannedMedicineEntity>
    suspend fun getAlarmData(id: Int): PlannedMedicineAlarmData
    fun getDetailsLive(id: Int): LiveData<PlannedMedicineDetails>
    fun getItemListLiveByDateAndPerson(date: AppDate, personID: Int): LiveData<List<PlannedMedicineItem>>
    fun getStatusByTaken(entity: PlannedMedicineEntity, taken: Boolean): PlannedMedicineEntity.StatusOfTaking
    fun getStatusByCurrDate(entity: PlannedMedicineEntity): PlannedMedicineEntity.StatusOfTaking
    fun updateStatusOfTakingByCurrDate(entityList: List<PlannedMedicineEntity>): List<PlannedMedicineEntity>
}

class PlannedMedicineServiceImpl(
    private val plannedMedicineDao: PlannedMedicineDao,
    private val medicinePlanDao: MedicinePlanDao,
    private val dateTimeService: DateTimeService,
    private val medicineSchedulerService: MedicineSchedulerService,
    private val deletedHistory: DeletedHistory
) : PlannedMedicineService {

    override suspend fun insert(entity: PlannedMedicineEntity) = plannedMedicineDao.insert(entity)

    override suspend fun insert(entityList: List<PlannedMedicineEntity>) = plannedMedicineDao.insert(entityList)

    override suspend fun insertForMedicinePlan(medicinePlanId: Int) {
        val medicinePlanEditData = medicinePlanDao.getEditDataById(medicinePlanId)
        val plannedMedicineList = medicineSchedulerService.getPlannedMedicinesForMedicinePlan(medicinePlanEditData)
        plannedMedicineDao.insert(plannedMedicineList)
    }

    override suspend fun update(entity: PlannedMedicineEntity) =
        plannedMedicineDao.update(entity.apply { synchronizedWithServer = false })

    override suspend fun update(entityList: List<PlannedMedicineEntity>) = plannedMedicineDao.update(entityList.map {
        it.apply {
            synchronizedWithServer = false
        }
    })

    override suspend fun updateForMedicinePlan(medicinePlanId: Int) {
        val medicinePlanEditData = medicinePlanDao.getEditDataById(medicinePlanId)
        val currDate = dateTimeService.getCurrDate()
        plannedMedicineDao.deleteFromDateByMedicinePlanID(currDate, medicinePlanEditData.medicinePlanID)
        val medicinePlanFromNow = medicinePlanEditData.copy(startDate = currDate)
        val plannedMedicineList = medicineSchedulerService.getPlannedMedicinesForMedicinePlan(medicinePlanFromNow)
        plannedMedicineDao.insert(plannedMedicineList)
    }

    override suspend fun updateAllStatus() {
        val updatedList = plannedMedicineDao.getEntityList().map { entity ->
            entity.apply {
                statusOfTaking = getStatusByCurrDate(entity)
            }
        }
        plannedMedicineDao.update(updatedList)
    }

    override suspend fun delete(idList: List<Int>) {
        idList.forEach { plannedMedicineID ->
            plannedMedicineDao.getRemoteId(plannedMedicineID)?.let { deletedHistory.addToPlannedMedicineHistory(it) }
        }
        plannedMedicineDao.delete(idList)
    }

    override suspend fun deleteFromDateByMedicinePlanID(date: AppDate, id: Int) {

    }

    override suspend fun getEntity(id: Int) = plannedMedicineDao.getEntityById(id)

    override suspend fun getEntityList() = plannedMedicineDao.getEntityList()

    override suspend fun getAlarmData(id: Int) = plannedMedicineDao.getAlarmData(id)

    override fun getDetailsLive(id: Int) = plannedMedicineDao.getDetailsLive(id)

    override fun getItemListLiveByDateAndPerson(date: AppDate, personID: Int) =
        plannedMedicineDao.getItemListLiveByDate(date, personID)

    override fun getStatusByTaken(entity: PlannedMedicineEntity, taken: Boolean): PlannedMedicineEntity.StatusOfTaking {
        return if (taken) {
            PlannedMedicineEntity.StatusOfTaking.TAKEN
        } else {
            getStatusByCurrDate(entity)
        }
    }

    override fun getStatusByCurrDate(entity: PlannedMedicineEntity): PlannedMedicineEntity.StatusOfTaking {
        return if (entity.statusOfTaking == PlannedMedicineEntity.StatusOfTaking.TAKEN) {
            PlannedMedicineEntity.StatusOfTaking.TAKEN
        } else {
            val currDate = dateTimeService.getCurrDate()
            val currTime = dateTimeService.getCurrTime()
            when {
                entity.plannedDate > currDate -> PlannedMedicineEntity.StatusOfTaking.WAITING
                entity.plannedDate < currDate -> PlannedMedicineEntity.StatusOfTaking.NOT_TAKEN
                else -> when {
                    entity.plannedTime < currTime -> PlannedMedicineEntity.StatusOfTaking.NOT_TAKEN
                    else -> PlannedMedicineEntity.StatusOfTaking.WAITING
                }
            }
        }
    }

    override fun updateStatusOfTakingByCurrDate(entityList: List<PlannedMedicineEntity>): List<PlannedMedicineEntity> {
        return entityList.map { entity ->
            entity.apply {
                statusOfTaking = getStatusByCurrDate(entity)
            }
        }
    }
}