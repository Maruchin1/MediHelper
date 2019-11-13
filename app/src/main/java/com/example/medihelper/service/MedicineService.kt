package com.example.medihelper.service

import android.content.Intent
import androidx.lifecycle.LiveData
import com.example.medihelper.localdata.AppSharedPref
import com.example.medihelper.localdata.DeletedHistory
import com.example.medihelper.localdata.MedicineImageFiles
import com.example.medihelper.localdata.dao.MedicineDao
import com.example.medihelper.localdata.entity.MedicineEntity
import com.example.medihelper.localdata.pojo.MedicineDetails
import com.example.medihelper.localdata.pojo.MedicineEditData
import com.example.medihelper.localdata.pojo.MedicineItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

interface MedicineService {
    suspend fun save(editData: MedicineEditData)
    suspend fun reduceCurrState(medicineId: Int, doseSize: Float)
    suspend fun increaseCurrState(medicineId: Int, doseSize: Float)
    suspend fun delete(id: Int)
    suspend fun getDetails(id: Int): MedicineDetails
    fun getItemListLive(): LiveData<List<MedicineItem>>
    fun getFilteredItemListLive(searchQuery: String): LiveData<List<MedicineItem>>
    fun getItemLive(id: Int): LiveData<MedicineItem>
    fun getDetailsLive(id: Int): LiveData<MedicineDetails>
    fun getImageFile(imageName: String): File
    fun getTempImageCaptureIntentAndFileLive(): Pair<Intent, LiveData<File>>
    fun getMedicineUnitList(): List<String>
    fun getMedicineUnitListLive(): LiveData<List<String>>
    fun saveMedicineUnitList(newList: List<String>)
}

class MedicineServiceImpl(
    private val medicineDao: MedicineDao,
    private val appSharedPref: AppSharedPref,
    private val deletedHistory: DeletedHistory,
    private val medicineImageFiles: MedicineImageFiles
) : MedicineService {

    override suspend fun save(editData: MedicineEditData) {
        val entity = MedicineEntity(
            medicineId = editData.medicineId,
            medicineName = editData.medicineName,
            expireDate = editData.expireDate,
            medicineUnit = editData.medicineUnit,
            packageSize = editData.packageSize,
            currState = editData.currState,
            additionalInfo = editData.additionalInfo,
            imageName = editData.imageName,
            synchronizedWithServer = false
        )
        if (editData.medicineId == 0) {
            medicineDao.insert(entity)
        } else {
            medicineDao.update(entity)
        }
    }

    override suspend fun reduceCurrState(medicineId: Int, doseSize: Float) {
        val entity = medicineDao.getEntity(medicineId)
        val currState = entity.currState
        if (currState != null) {
            var newState = currState - doseSize
            if (newState < 0f) {
                newState = 0f
            }
            entity.currState = newState
            medicineDao.update(entity)
        }
    }

    override suspend fun increaseCurrState(medicineId: Int, doseSize: Float) {
        val entity = medicineDao.getEntity(medicineId)
        val currState = entity.currState
        if (currState != null) {
            var newState = currState + doseSize
            val packageSize = entity.packageSize
            if (packageSize != null && newState > packageSize) {
                newState = packageSize
            }
            entity.currState = newState
            medicineDao.update(entity)
        }
    }

    override suspend fun delete(id: Int) {
        medicineDao.getRemoteIdById(id)?.let { deletedHistory.addToMedicineHistory(it) }
        medicineDao.delete(id)
    }

    override suspend fun getDetails(id: Int) = medicineDao.getDetails(id)

    override fun getItemListLive() = medicineDao.getItemListLive()

    override fun getFilteredItemListLive(searchQuery: String) = medicineDao.getFilteredItemListLive(searchQuery)

    override fun getItemLive(id: Int) = medicineDao.getItemLive(id)

    override fun getDetailsLive(id: Int) = medicineDao.getDetailsLive(id)

    override fun getImageFile(imageName: String) = medicineImageFiles.getImageFile(imageName)

    override fun getTempImageCaptureIntentAndFileLive() = medicineImageFiles.getTempImageCaptureIntentAndFileLive()

    override fun getMedicineUnitList() = appSharedPref.getMedicineUnitList()

    override fun getMedicineUnitListLive() = appSharedPref.getMedicineUnitListLive()

    override fun saveMedicineUnitList(newList: List<String>) = appSharedPref.saveMedicineUnitList(newList)
}