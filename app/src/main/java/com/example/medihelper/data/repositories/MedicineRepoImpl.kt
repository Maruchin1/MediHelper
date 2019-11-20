package com.example.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.data.local.SharedPref
import com.example.medihelper.data.local.dao.MedicineDao
import com.example.medihelper.data.local.model.MedicineEntity
import com.example.medihelper.domain.entities.Medicine
import com.example.medihelper.domain.repositories.MedicineRepo

class MedicineRepoImpl(
    private val medicineDao: MedicineDao,
    private val sharedPref: SharedPref
) : MedicineRepo {

    override suspend fun insert(medicine: Medicine) {
        val newEntity = MedicineEntity(medicine = medicine, medicineRemoteId = null)
        medicineDao.insert(newEntity)
    }

    override suspend fun update(medicine: Medicine) {
        val existingRemoteId = medicineDao.getRemoteIdById(medicine.medicineId)
        val updatedEntity = MedicineEntity(medicine = medicine, medicineRemoteId = existingRemoteId)
        medicineDao.update(updatedEntity)
    }

    override suspend fun deleteById(id: Int) {
        medicineDao.deleteById(id)
    }

    override suspend fun getById(id: Int): Medicine {
        val entity = medicineDao.getById(id)
        return entity.toMedicine()
    }

    override fun getLiveById(id: Int): LiveData<Medicine> {
        val entityLive = medicineDao.getLiveById(id)
        return Transformations.map(entityLive) { it.toMedicine() }
    }

    override fun getAllListLive(): LiveData<List<Medicine>> {
        val entityListLive = medicineDao.getAllListLive()
        return Transformations.map(entityListLive) { list ->
            list.map { it.toMedicine() }
        }
    }

    override fun getListLiveFilteredByName(nameQuery: String): LiveData<List<Medicine>> {
        val entityListLive = medicineDao.getListLiveFilteredByName(nameQuery)
        return Transformations.map(entityListLive) { list ->
            list.map { it.toMedicine() }
        }
    }

    override fun getUnitList(): List<String> {
        return sharedPref.getMedicineUnitList()
    }
}