package com.example.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.data.local.DeletedHistory
import com.example.medihelper.data.local.ImagesFiles
import com.example.medihelper.data.local.SharedPref
import com.example.medihelper.data.local.dao.MedicineDao
import com.example.medihelper.data.local.model.MedicineEntity
import com.example.medihelper.domain.entities.Medicine
import com.example.medihelper.domain.repositories.MedicineRepo

class MedicineRepoImpl(
    private val medicineDao: MedicineDao,
    private val sharedPref: SharedPref,
    private val deletedHistory: DeletedHistory,
    private val imagesFiles: ImagesFiles
) : MedicineRepo {

    init {
        checkDefaultUnitList()
    }

    override suspend fun insert(medicine: Medicine) {
        val permaFileName = if (medicine.image != null) {
            imagesFiles.saveTempImageFileAsPerma(medicine.name, medicine.image)
        } else null

        val newEntity = MedicineEntity(medicine = medicine, permaFileName = permaFileName)
        medicineDao.insert(newEntity)
    }

    override suspend fun update(medicine: Medicine) {
        val permaFileName = if (medicine.image == null) {
            null
        } else if (!imagesFiles.isTempFile(medicine.image)) {
            medicine.image.name
        } else {
            imagesFiles.saveTempImageFileAsPerma(medicine.name, medicine.image)
        }

        val existingEntity = medicineDao.getById(medicine.medicineId)
        val existingImageName = existingEntity.imageName
        if (existingImageName != null && existingImageName != permaFileName) {
            val existingFile = imagesFiles.getImageFile(existingImageName)
            existingFile.delete()
        }

        existingEntity.update(medicine, permaFileName)
        medicineDao.update(existingEntity)
    }

    override suspend fun deleteById(id: Int) {
        val remoteId = medicineDao.getRemoteIdById(id)
        if (remoteId != null) {
            deletedHistory.addToMedicineHistory(remoteId)
        }
        medicineDao.deleteById(id)
    }

    override suspend fun getById(id: Int): Medicine {
        val entity = medicineDao.getById(id)
        return entity.toMedicine(imagesFiles)
    }

    override fun getLiveById(id: Int): LiveData<Medicine> {
        val entityLive = medicineDao.getLiveById(id)
        return Transformations.map(entityLive) { it.toMedicine(imagesFiles) }
    }

    override fun getAllListLive(): LiveData<List<Medicine>> {
        val entityListLive = medicineDao.getAllListLive()
        return Transformations.map(entityListLive) { list ->
            list.map { it.toMedicine(imagesFiles) }
        }
    }

    override fun getListLiveFilteredByName(nameQuery: String): LiveData<List<Medicine>> {
        val entityListLive = medicineDao.getListLiveFilteredByName(nameQuery)
        return Transformations.map(entityListLive) { list ->
            list.map { it.toMedicine(imagesFiles) }
        }
    }

    override fun getUnitList(): List<String> {
        return sharedPref.getMedicineUnitList()
    }

    override fun getUnitListLive(): LiveData<List<String>> {
        return sharedPref.getMedicineUnitListLive()
    }

    override fun saveUnitList(list: List<String>) {
        sharedPref.saveMedicineUnitList(list)
    }

    private fun checkDefaultUnitList() = with(sharedPref) {
        if (getMedicineUnitList().isNullOrEmpty()) {
            saveMedicineUnitList(getDefaultUnitList())
        }
    }

    private fun getDefaultUnitList() = listOf("dawki", "tabletki", "ml", "g", "mg", "pastylki")
}