package com.example.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.data.local.DeletedHistory
import com.example.medihelper.data.local.ImagesFiles
import com.example.medihelper.data.local.dao.MedicinePlanDao
import com.example.medihelper.data.local.dao.TimeDoseDao
import com.example.medihelper.data.local.model.MedicinePlanEntity
import com.example.medihelper.data.local.model.TimeDoseEntity
import com.example.medihelper.domain.entities.MedicinePlan
import com.example.medihelper.domain.entities.MedicinePlanWithMedicine
import com.example.medihelper.domain.entities.MedicinePlanWithMedicineAndPlannedMedicines
import com.example.medihelper.domain.repositories.MedicinePlanRepo

class MedicinePlanRepoImpl(
    private val medicinePlanDao: MedicinePlanDao,
    private val timeDoseDao: TimeDoseDao,
    private val deletedHistory: DeletedHistory,
    private val imagesFiles: ImagesFiles
) : MedicinePlanRepo {


    override suspend fun insert(medicinePlan: MedicinePlan): Int {
        val newEntity = MedicinePlanEntity(medicinePlan = medicinePlan)
        val insertedId = medicinePlanDao.insert(newEntity).toInt()

        val timeDoseEntityList = medicinePlan.timeDoseList.map { TimeDoseEntity(it, insertedId) }
        timeDoseDao.insert(timeDoseEntityList)

        return insertedId
    }

    override suspend fun update(medicinePlan: MedicinePlan) {
        val existingEntity = medicinePlanDao.getById(medicinePlan.medicinePlanId)
        existingEntity.update(medicinePlan)
        medicinePlanDao.update(existingEntity)

        val timeDoseEntityList = medicinePlan.timeDoseList.map { TimeDoseEntity(it, existingEntity.medicinePlanId) }
        timeDoseDao.deleteByMedicinePlanId(existingEntity.medicinePlanId)
        timeDoseDao.insert(timeDoseEntityList)
    }

    override suspend fun deleteById(id: Int) {
        val remoteId = medicinePlanDao.getRemoteIdById(id)
        if (remoteId != null) {
            deletedHistory.addToMedicinePlanHistory(remoteId)
        }
        medicinePlanDao.deleteById(id)
    }

    override suspend fun getById(id: Int): MedicinePlan {
        val entity = medicinePlanDao.getWithTimeDoseListById(id)
        return entity.toMedicinePlan()
    }

    override fun getWithMedicineAndPlannedMedicinesLiveById(id: Int): LiveData<MedicinePlanWithMedicineAndPlannedMedicines> {
        val entity = medicinePlanDao.getWithTimeDoseListAndMedicineAndPlannedMedicineListLiveById(id)
        return Transformations.map(entity) { it.toMedicinePlanWithMedicineAndPlannedMedicines(imagesFiles) }
    }

    override fun getWithMedicineListLiveByPersonId(id: Int): LiveData<List<MedicinePlanWithMedicine>> {
        val entityList = medicinePlanDao.getWithTimeDoseListAndMedicineListLiveByPersonId(id)
        return Transformations.map(entityList) { list ->
            list.map { it.toMedicinePlanWithMedicine(imagesFiles) }
        }
    }
}