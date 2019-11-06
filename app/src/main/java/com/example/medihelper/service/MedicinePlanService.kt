package com.example.medihelper.service

import androidx.lifecycle.LiveData
import com.example.medihelper.localdatabase.DeletedHistory
import com.example.medihelper.localdatabase.dao.MedicinePlanDao
import com.example.medihelper.localdatabase.dao.TimeDoseDao
import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.example.medihelper.localdatabase.entity.TimeDoseEntity
import com.example.medihelper.localdatabase.pojo.MedicinePlanEditData
import com.example.medihelper.localdatabase.pojo.MedicinePlanHistory
import com.example.medihelper.localdatabase.pojo.MedicinePlanItem
import com.example.medihelper.localdatabase.pojo.TimeDoseEditData

interface MedicinePlanService {
    suspend fun save(editData: MedicinePlanEditData)
    suspend fun delete(medicinePlanID: Int)
    suspend fun getEntity(medicinePlanID: Int): MedicinePlanEntity
    suspend fun getEntityList(): List<MedicinePlanEntity>
    suspend fun getEditData(medicinePlanID: Int): MedicinePlanEditData
    fun getItemListLive(personID: Int): LiveData<List<MedicinePlanItem>>
    fun getHistoryLive(medicinePlanID: Int): LiveData<MedicinePlanHistory>
}

class MedicinePlanServiceImpl(
    private val medicinePlanDao: MedicinePlanDao,
    private val timeDoseDao: TimeDoseDao,
    private val deletedHistory: DeletedHistory,
    private val plannedMedicineService: PlannedMedicineService
) : MedicinePlanService {

    override suspend fun save(editData: MedicinePlanEditData) {
        val entity = MedicinePlanEntity(
            medicinePlanID = editData.medicinePlanID,
            medicineID = editData.medicineID,
            personID = editData.personID,
            durationType = editData.durationType,
            startDate = editData.startDate,
            endDate = editData.endDate,
            daysType = editData.daysType,
            daysOfWeek = editData.daysOfWeek,
            intervalOfDays = editData.intervalOfDays,
            synchronizedWithServer = false
        )
        if (entity.medicinePlanID == 0) {
            val insertedId = medicinePlanDao.insert(entity).toInt()
            val timeDoseEntityList = mapTimeDoseEditDataListToEntityList(editData.timeDoseList, insertedId)
            timeDoseDao.insert(timeDoseEntityList)
            plannedMedicineService.updateForMedicinePlan(insertedId)
        } else {
            medicinePlanDao.update(entity)
            val timeDoseEntityList = mapTimeDoseEditDataListToEntityList(editData.timeDoseList, entity.medicinePlanID)
            timeDoseDao.deleteAllByMedicinePlanId(entity.medicinePlanID)
            timeDoseDao.insert(timeDoseEntityList)
            plannedMedicineService.updateForMedicinePlan(entity.medicinePlanID)
        }
    }

    override suspend fun delete(medicinePlanID: Int) {
        medicinePlanDao.getRemoteIdById(medicinePlanID)?.let { deletedHistory.addToMedicinePlanHistory(it) }
        medicinePlanDao.delete(medicinePlanID)
    }

    override suspend fun getEntity(medicinePlanID: Int) = medicinePlanDao.getEntity(medicinePlanID)

    override suspend fun getEntityList() = medicinePlanDao.getEntityList()

    override suspend fun getEditData(medicinePlanID: Int) = medicinePlanDao.getEditDataById(medicinePlanID)

    override fun getItemListLive(personID: Int) = medicinePlanDao.getItemListLiveByPersonId(personID)

    override fun getHistoryLive(medicinePlanID: Int) = medicinePlanDao.getHistoryLive(medicinePlanID)

    private fun mapTimeDoseEditDataListToEntityList(
        editDataList: List<TimeDoseEditData>,
        medicinePlanID: Int
    ) = editDataList.map {
        TimeDoseEntity(
            time = it.time,
            doseSize = it.doseSize,
            medicinePlanId = medicinePlanID
        )
    }
}