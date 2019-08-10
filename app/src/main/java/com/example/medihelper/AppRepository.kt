package com.example.medihelper

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medihelper.localdatabase.LocalDatabase
import com.example.medihelper.localdatabase.PlannedMedicineScheduler
import com.example.medihelper.localdatabase.dao.MedicineDAO
import com.example.medihelper.localdatabase.dao.MedicinePlanDAO
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object AppRepository {
    private val TAG = AppRepository::class.simpleName

    private lateinit var sharedPreferences: SharedPreferences
    private val medicineUnitListLive = MutableLiveData<List<String>>()

    private lateinit var medicineDao: MedicineDAO
    private lateinit var medicinePlanDao: MedicinePlanDAO
    private lateinit var plannedMedicineDao: com.example.medihelper.localdatabase.dao.PlannedMedicineDAO
    private var photosDir: File? = null

    fun init(app: Application) {
        Log.d(TAG, "init")
        initSharedPreferences(app)
        initDatabaseData(app)
        photosDir = app.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        insertInitialMedicinesTypes()
    }

    // SharedPreferences
    fun getMedicineUnitListLive(): LiveData<List<String>> = medicineUnitListLive

    // Database
    // Get
    fun getMedicineKitItemLive(medicineID: Int) = medicineDao.getKitItemLive(medicineID)

    fun getMedicineKitItemListLive() = medicineDao.getKitItemListLive()

    fun getMedicineEditDataLive(medicineID: Int) = medicineDao.getEditDataLive(medicineID)

    fun getMedicineDetailsLive(medicineID: Int) = medicineDao.getMedicineDetailsLive(medicineID)

    fun getMedicinePlanListLive() = medicinePlanDao.getMedicinePlanListLive()

    fun getPlannedMedicine(plannedMedicineID: Int) = plannedMedicineDao.getByID(plannedMedicineID)

    fun getPlannedMedicineDetailsLive(plannedMedicineID: Int) = plannedMedicineDao.getPlannedMedicineDetailsLive(plannedMedicineID)

    fun getPlannedMedicineItemListLiveByDate(date: Date) = plannedMedicineDao.getPlannedMedicineByDateListLive(date)

    // Delete
    fun deleteMedicine(medicineID: Int) = AsyncTask.execute { medicineDao.delete(medicineID) }

    fun deleteMedicinePlan(medicinePlanID: Int) = AsyncTask.execute { medicinePlanDao.delete(medicinePlanID) }

    // Update
    fun updateMedicine(medicineEntity: MedicineEntity) = AsyncTask.execute {
        medicineDao.update(medicineEntity)
    }

    fun updatePlannedMedicine(plannedMedicineEntity: PlannedMedicineEntity) = AsyncTask.execute {
        plannedMedicineDao.update(plannedMedicineEntity)
    }

    // Insert
    fun insertMedicine(medicineEntity: MedicineEntity) = AsyncTask.execute {
        medicineDao.insert(medicineEntity)
    }

    fun insertMedicinePlan(medicinePlanEntity: MedicinePlanEntity) = AsyncTask.execute {
        val addedMedicinePlanID = medicinePlanDao.insert(medicinePlanEntity)
        val addedMedicinePlan = medicinePlanDao.getByID(addedMedicinePlanID.toInt())
        val plannedMedicineList = PlannedMedicineScheduler().getPlannedMedicineList(addedMedicinePlan)
        plannedMedicineDao.insert(plannedMedicineList)
        updatePlannedMedicinesStatuses()
    }

    fun createTempPhotoFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File.createTempFile(
            "TMP_JPEG_${timeStamp}_",
            ".jpg",
            photosDir
        ).apply {
            deleteOnExit()
        }
    }

    fun createPhotoFileFromTemp(tmpFile: File): File {
        val fileName = tmpFile.name.replace("TMP_", "")
        val file = File(photosDir, fileName)
        tmpFile.copyTo(file)
        return file
    }

    fun updatePlannedMedicinesStatuses() = AsyncTask.execute {
        plannedMedicineDao.getAll().forEach { plannedMedicine ->
            plannedMedicine.updateStatusByCurrDate()
        }
    }

    private fun initDatabaseData(app: Application) {
        val database = LocalDatabase.getInstance(app.applicationContext)
        medicineDao = database.medicineDao()
        medicinePlanDao = database.medicinePlanDao()
        plannedMedicineDao = database.plannedMedicineDao()
    }

    private fun initSharedPreferences(app: Application) {
        sharedPreferences = app.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        medicineUnitListLive.value = sharedPreferences.getStringSet(KEY_MEDICINE_UNIT_SET, null)?.toList()
        sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                KEY_MEDICINE_UNIT_SET -> medicineUnitListLive.value = sharedPreferences.getStringSet(key, null)?.toList()
            }
        }
    }

    private fun insertInitialMedicinesTypes() = AsyncTask.execute {
        val medicineUnitSet = sharedPreferences.getStringSet(KEY_MEDICINE_UNIT_SET, null)
        if (medicineUnitSet.isNullOrEmpty()) {
            val unitSet = mutableSetOf("pigułki", "ml", "g", "mg", "krople")
            sharedPreferences.edit {
                putStringSet(KEY_MEDICINE_UNIT_SET, unitSet)
                apply()
            }
        }
    }

    private const val APP_SHARED_PREFERENCES = "app-shared-preferences"
    private const val KEY_MEDICINE_UNIT_SET = "key-medicine-type-list"
}