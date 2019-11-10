package com.example.medihelper.service

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.custom.SharedPrefLiveData
import com.example.medihelper.localdatabase.DeletedHistory
import com.example.medihelper.localdatabase.dao.MedicineDao
import com.example.medihelper.localdatabase.entity.MedicineEntity
import com.example.medihelper.localdatabase.pojo.MedicineDetails
import com.example.medihelper.localdatabase.pojo.MedicineEditData
import com.example.medihelper.localdatabase.pojo.MedicineItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

interface MedicineService {
    suspend fun save(editData: MedicineEditData)
    suspend fun reduceCurrState(medicineId: Int, doseSize: Float)
    suspend fun increaseCurrState(medicineId: Int, doseSize: Float)
    suspend fun delete(id: Int)
    suspend fun getEntity(id: Int): MedicineEntity
    suspend fun getEntityList(): List<MedicineEntity>
    suspend fun getDetails(id: Int): MedicineDetails
    fun getItemListLive(): LiveData<List<MedicineItem>>
    fun getFilteredItemListLive(searchQuery: String): LiveData<List<MedicineItem>>
    fun getItemLive(id: Int): LiveData<MedicineItem>
    fun getDetailsLive(id: Int): LiveData<MedicineDetails>
    fun getImageFile(imageName: String): File
    fun createTempImageFile(): File
    fun saveTmpFile(medicineName: String, tempFile: File): String
    fun getMedicineUnitList(): List<String>
    fun getMedicineUnitListLive(): LiveData<List<String>>
    fun saveMedicineUnitList(newList: List<String>)
}

class MedicineServiceImpl(
    private val medicineDao: MedicineDao,
    private val appFilesDir: File,
    private val externalImagesDir: File,
    private val sharedPreferences: SharedPreferences,
    private val deletedHistory: DeletedHistory
) : MedicineService {

    companion object {
        private const val KEY_MEDICINE_UNIT_SET = "key-medicine-type-list"
    }

    override suspend fun save(editData: MedicineEditData) {
        val entity = MedicineEntity(
            medicineID = editData.medicineID,
            medicineName = editData.medicineName,
            expireDate = editData.expireDate,
            medicineUnit = editData.medicineUnit,
            packageSize = editData.packageSize,
            currState = editData.currState,
            additionalInfo = editData.additionalInfo,
            imageName = editData.imageName,
            synchronizedWithServer = false
        )
        if (editData.medicineID == 0) {
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

    override suspend fun getEntity(id: Int) = medicineDao.getEntity(id)

    override suspend fun getEntityList() = medicineDao.getEntityList()

    override suspend fun getDetails(id: Int) = medicineDao.getDetails(id)

    override fun getItemListLive() = medicineDao.getItemListLive()

    override fun getFilteredItemListLive(searchQuery: String) = medicineDao.getFilteredItemListLive(searchQuery)

    override fun getItemLive(id: Int) = medicineDao.getItemLive(id)

    override fun getDetailsLive(id: Int) = medicineDao.getDetailsLive(id)

    override fun getImageFile(imageName: String) = File(appFilesDir, imageName)

    override fun createTempImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile(
            "TMP_${timeStamp}",
            ".jpg",
            externalImagesDir
        ).apply {
            deleteOnExit()
        }
    }

    override fun saveTmpFile(medicineName: String, tempFile: File): String {
        val fileName = medicineName + tempFile.name.replace("TMP", "")
        val file = File(appFilesDir, fileName)
        tempFile.copyTo(file)
        return fileName
    }

    override fun getMedicineUnitList() =
        sharedPreferences.getStringSet(KEY_MEDICINE_UNIT_SET, null)?.toList() ?: emptyList()

    override fun getMedicineUnitListLive(): LiveData<List<String>> {
        return Transformations.map(SharedPrefLiveData(sharedPreferences, KEY_MEDICINE_UNIT_SET, emptySet<String>())) {
            it.toList()
        }
    }

    override fun saveMedicineUnitList(newList: List<String>) = sharedPreferences.edit(commit = true) {
        putStringSet(KEY_MEDICINE_UNIT_SET, newList.toMutableSet())
    }
}