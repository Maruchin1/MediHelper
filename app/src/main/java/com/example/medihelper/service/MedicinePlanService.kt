package com.example.medihelper.service

import androidx.lifecycle.LiveData
import com.example.medihelper.localdata.DeletedHistory
import com.example.medihelper.localdata.dao.MedicinePlanDao
import com.example.medihelper.localdata.dao.TimeDoseDao
import com.example.medihelper.localdata.entity.MedicinePlanEntity
import com.example.medihelper.localdata.entity.TimeDoseEntity
import com.example.medihelper.localdata.pojo.MedicinePlanEditData
import com.example.medihelper.localdata.pojo.MedicinePlanHistory
import com.example.medihelper.localdata.pojo.MedicinePlanItem
import com.example.medihelper.localdata.pojo.TimeDoseEditData

interface MedicinePlanService {
    suspend fun save(editData: MedicinePlanEditData)
    suspend fun delete(medicinePlanID: Int)
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
            medicinePlanId = editData.medicinePlanId,
            medicineId = editData.medicineId,
            personId = editData.personId,
            durationType = editData.durationType,
            startDate = editData.startDate,
            endDate = editData.endDate,
            daysType = editData.daysType,
            daysOfWeek = editData.daysOfWeek,
            intervalOfDays = editData.intervalOfDays,
            synchronizedWithServer = false
        )
        if (entity.medicinePlanId == 0) {
            val insertedId = medicinePlanDao.insert(entity).toInt()
            val timeDoseEntityList = mapTimeDoseEditDataListToEntityList(editData.timeDoseList, insertedId)
            timeDoseDao.insert(timeDoseEntityList)
            plannedMedicineService.updateForMedicinePlan(insertedId)
        } else {
            medicinePlanDao.update(entity)
            val timeDoseEntityList = mapTimeDoseEditDataListToEntityList(editData.timeDoseList, entity.medicinePlanId)
            timeDoseDao.deleteAllByMedicinePlanId(entity.medicinePlanId)
            timeDoseDao.insert(timeDoseEntityList)
            plannedMedicineService.updateForMedicinePlan(entity.medicinePlanId)
        }
    }

    override suspend fun delete(medicinePlanID: Int) {
        medicinePlanDao.getRemoteIdById(medicinePlanID)?.let { deletedHistory.addToMedicinePlanHistory(it) }
        medicinePlanDao.delete(medicinePlanID)
    }

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