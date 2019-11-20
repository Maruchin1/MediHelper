package com.example.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.data.local.dao.PlannedMedicineDao
import com.example.medihelper.data.local.model.PlannedMedicineEntity
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.PlannedMedicine
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicine
import com.example.medihelper.domain.repositories.PlannedMedicineRepo

class PlannedMedicineRepoImpl(
    private val plannedMedicineDao: PlannedMedicineDao
) : PlannedMedicineRepo {

    override suspend fun insert(plannedMedicineList: List<PlannedMedicine>) {
        val newEntityList = plannedMedicineList.map {
            PlannedMedicineEntity(plannedMedicine = it, remoteId = null)
        }
        plannedMedicineDao.insert(newEntityList)
    }

    override suspend fun update(plannedMedicine: PlannedMedicine) {
        val existingRemoteId = plannedMedicineDao.getRemoteIdById(plannedMedicine.plannedMedicineId)
        val updatedEntity = PlannedMedicineEntity(plannedMedicine = plannedMedicine, remoteId = existingRemoteId)
        plannedMedicineDao.update(updatedEntity)
    }

    override suspend fun update(plannedMedicineList: List<PlannedMedicine>) {
        val updatedEntityList = plannedMedicineList.map {
            val existingRemoteId = plannedMedicineDao.getRemoteIdById(it.plannedMedicineId)
            PlannedMedicineEntity(plannedMedicine = it, remoteId = existingRemoteId)
        }
        plannedMedicineDao.update(updatedEntityList)
    }

    override suspend fun deleteFromDateByMedicinePlanId(date: AppDate, medicinePlanId: Int) {
        plannedMedicineDao.deleteFromDateByMedicinePlanId(date, medicinePlanId)
    }

    override suspend fun getById(id: Int): PlannedMedicine {
        val entity = plannedMedicineDao.getById(id)
        return entity.toPlannedMedicine()
    }

    override suspend fun getAllList(): List<PlannedMedicine> {
        val entityList = plannedMedicineDao.getAllList()
        return entityList.map { it.toPlannedMedicine() }
    }

    override fun getWithMedicineLiveById(id: Int): LiveData<PlannedMedicineWithMedicine> {
        val entityLive = plannedMedicineDao.getWithMedicineLiveById(id)
        return Transformations.map(entityLive) {
            it.toPlannedMedicineWithMedicine()
        }
    }

    override fun getWithMedicineListLiveByDateAndPerson(
        date: AppDate,
        personId: Int
    ): LiveData<List<PlannedMedicineWithMedicine>> {
        val entityListLive = plannedMedicineDao.getWithMedicineListLiveByDateAndPerson(date, personId)
        return Transformations.map(entityListLive) { list ->
            list.map { it.toPlannedMedicineWithMedicine() }
        }
    }
}