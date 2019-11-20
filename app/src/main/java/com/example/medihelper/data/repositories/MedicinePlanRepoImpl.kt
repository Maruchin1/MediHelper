package com.example.medihelper.data.repositories

import androidx.lifecycle.LiveData
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
    private val timeDoseDao: TimeDoseDao
) : MedicinePlanRepo {


    override suspend fun insert(medicinePlan: MedicinePlan) {
        val newEntity = MedicinePlanEntity(medicinePlan = medicinePlan, remoteId = null)
        val insertedId = medicinePlanDao.insert(newEntity).toInt()

        val timeDoseEntityList = medicinePlan.timeDoseList.map { TimeDoseEntity(it, insertedId) }
        timeDoseDao.insert(timeDoseEntityList)
    }

    override suspend fun update(medicinePlan: MedicinePlan) {
        val existingRemoteId = medicinePlanDao.getRemoteIdById(medicinePlan.medicinePlanId)
        val updatedEntity = MedicinePlanEntity(medicinePlan = medicinePlan, remoteId = existingRemoteId)
        medicinePlanDao.update(updatedEntity)

        val timeDoseEntityList = medicinePlan.timeDoseList.map { TimeDoseEntity(it, updatedEntity.medicinePlanId) }
        timeDoseDao.deleteByMedicinePlanId(updatedEntity.medicinePlanId)
        timeDoseDao.insert(timeDoseEntityList)
    }

    override suspend fun deleteById(id: Int) {
       medicinePlanDao.deleteById(id)
    }

    override suspend fun getById(id: Int): MedicinePlan {
        val entity = medicinePlanDao.getWithTimeDoseListById(id)
        return entity.toMedicinePlan()
    }

    override fun getWithMedicineAndPlannedMedicinesById(id: Int): LiveData<MedicinePlanWithMedicineAndPlannedMedicines> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWithMedicineListLiveByPersonId(id: Int): LiveData<List<MedicinePlanWithMedicine>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}