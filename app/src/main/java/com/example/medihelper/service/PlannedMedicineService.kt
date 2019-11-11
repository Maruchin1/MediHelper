package com.example.medihelper.service

import androidx.lifecycle.LiveData
import com.example.medihelper.localdata.type.AppDate
import com.example.medihelper.localdata.DeletedHistory
import com.example.medihelper.localdata.MedicineScheduler
import com.example.medihelper.localdata.dao.MedicinePlanDao
import com.example.medihelper.localdata.dao.PlannedMedicineDao
import com.example.medihelper.localdata.entity.PlannedMedicineEntity
import com.example.medihelper.localdata.pojo.PlannedMedicineAlarmData
import com.example.medihelper.localdata.pojo.PlannedMedicineDetails
import com.example.medihelper.localdata.pojo.PlannedMedicineItem
import com.example.medihelper.localdata.type.StatusOfTaking

interface PlannedMedicineService {
    suspend fun addForMedicinePlan(medicinePlanId: Int)
    suspend fun updateForMedicinePlan(medicinePlanId: Int)
    suspend fun updateAllStatus()
    suspend fun changeMedicineTaken(plannedMedicineId: Int, taken: Boolean)
    suspend fun delete(idList: List<Int>)
    suspend fun deleteFromDateByMedicinePlanID(date: AppDate, id: Int)
    suspend fun getAlarmData(id: Int): PlannedMedicineAlarmData
    fun getDetailsLive(id: Int): LiveData<PlannedMedicineDetails>
    fun getItemListLiveByDateAndPerson(date: AppDate, personID: Int): LiveData<List<PlannedMedicineItem>>
    fun updateStatusOfTakingByCurrDate(entityList: List<PlannedMedicineEntity>): List<PlannedMedicineEntity>
}

class PlannedMedicineServiceImpl(
    private val plannedMedicineDao: PlannedMedicineDao,
    private val medicinePlanDao: MedicinePlanDao,
    private val dateTimeService: DateTimeService,
    private val medicineScheduler: MedicineScheduler,
    private val deletedHistory: DeletedHistory
) : PlannedMedicineService {

    override suspend fun addForMedicinePlan(medicinePlanId: Int) {
        val medicinePlanEditData = medicinePlanDao.getEditDataById(medicinePlanId)
        val plannedMedicineList = medicineScheduler.getPlannedMedicinesForMedicinePlan(medicinePlanEditData)
        plannedMedicineDao.insert(plannedMedicineList)
    }

    override suspend fun updateForMedicinePlan(medicinePlanId: Int) {
        val medicinePlanEditData = medicinePlanDao.getEditDataById(medicinePlanId)
        val currDate = dateTimeService.getCurrDate()
        plannedMedicineDao.deleteFromDateByMedicinePlanID(currDate, medicinePlanEditData.medicinePlanId)
        val medicinePlanFromNow = medicinePlanEditData.copy(startDate = currDate)
        val plannedMedicineList = medicineScheduler.getPlannedMedicinesForMedicinePlan(medicinePlanFromNow)
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

    override suspend fun changeMedicineTaken(plannedMedicineId: Int, taken: Boolean) {
        val entity = plannedMedicineDao.getEntityById(plannedMedicineId)
        val newStatus = getStatusByTaken(entity, taken)
        entity.statusOfTaking = newStatus
        plannedMedicineDao.update(entity)
    }

    override suspend fun delete(idList: List<Int>) {
        idList.forEach { plannedMedicineID ->
            plannedMedicineDao.getRemoteId(plannedMedicineID)?.let { deletedHistory.addToPlannedMedicineHistory(it) }
        }
        plannedMedicineDao.delete(idList)
    }

    override suspend fun deleteFromDateByMedicinePlanID(date: AppDate, id: Int) {

    }

    override suspend fun getAlarmData(id: Int) = plannedMedicineDao.getAlarmData(id)

    override fun getDetailsLive(id: Int) = plannedMedicineDao.getDetailsLive(id)

    override fun getItemListLiveByDateAndPerson(date: AppDate, personID: Int) =
        plannedMedicineDao.getItemListLiveByDate(date, personID)

    override fun updateStatusOfTakingByCurrDate(entityList: List<PlannedMedicineEntity>): List<PlannedMedicineEntity> {
        return entityList.map { entity ->
            entity.apply {
                statusOfTaking = getStatusByCurrDate(entity)
            }
        }
    }

    private fun getStatusByTaken(entity: PlannedMedicineEntity, taken: Boolean): StatusOfTaking {
        return if (taken) {
           StatusOfTaking.TAKEN
        } else {
            getStatusByCurrDate(entity)
        }
    }

    private fun getStatusByCurrDate(entity: PlannedMedicineEntity): StatusOfTaking {
        return if (entity.statusOfTaking == StatusOfTaking.TAKEN) {
            StatusOfTaking.TAKEN
        } else {
            val currDate = dateTimeService.getCurrDate()
            val currTime = dateTimeService.getCurrTime()
            when {
                entity.plannedDate > currDate -> StatusOfTaking.WAITING
                entity.plannedDate < currDate -> StatusOfTaking.NOT_TAKEN
                else -> when {
                    entity.plannedTime < currTime -> StatusOfTaking.NOT_TAKEN
                    else ->StatusOfTaking.WAITING
                }
            }
        }
    }
}