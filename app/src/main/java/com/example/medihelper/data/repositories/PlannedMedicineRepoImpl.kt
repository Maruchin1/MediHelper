package com.example.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.data.local.DeletedHistory
import com.example.medihelper.data.local.ImagesFiles
import com.example.medihelper.data.local.dao.PlannedMedicineDao
import com.example.medihelper.data.local.model.PlannedMedicineEntity
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.PlannedMedicine
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicine
import com.example.medihelper.domain.repositories.PlannedMedicineRepo

class PlannedMedicineRepoImpl(
    private val plannedMedicineDao: PlannedMedicineDao,
    private val deletedHistory: DeletedHistory,
    private val imagesFiles: ImagesFiles
) : PlannedMedicineRepo {

    override suspend fun insert(plannedMedicineList: List<PlannedMedicine>) {
        val newEntityList = plannedMedicineList.map {
            PlannedMedicineEntity(plannedMedicine = it)
        }
        plannedMedicineDao.insert(newEntityList)
    }

    override suspend fun update(plannedMedicine: PlannedMedicine) {
        val existingEntity = plannedMedicineDao.getById(plannedMedicine.plannedMedicineId)
        existingEntity.update(plannedMedicine)
        plannedMedicineDao.update(existingEntity)
    }

    override suspend fun update(plannedMedicineList: List<PlannedMedicine>) {
        val updatedEntityList = plannedMedicineList.map {
            val existingEntity = plannedMedicineDao.getById(it.plannedMedicineId)
            existingEntity.update(it)
            existingEntity
        }
        plannedMedicineDao.update(updatedEntityList)
    }

    override suspend fun deleteFromDateByMedicinePlanId(date: AppDate, medicinePlanId: Int) {
        val remoteIdList = plannedMedicineDao.getRemoteIdListFromDateByMedicinePlanId(date, medicinePlanId)
        remoteIdList.forEach {
            deletedHistory.addToPlannedMedicineHistory(it)
        }
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
            it.toPlannedMedicineWithMedicine(imagesFiles)
        }
    }

    override fun getWithMedicineListLiveByDateAndPerson(
        date: AppDate,
        personId: Int
    ): LiveData<List<PlannedMedicineWithMedicine>> {
        val entityListLive = plannedMedicineDao.getWithMedicineListLiveByDateAndPerson(date, personId)
        return Transformations.map(entityListLive) { list ->
            list.map { it.toPlannedMedicineWithMedicine(imagesFiles) }
        }
    }
}