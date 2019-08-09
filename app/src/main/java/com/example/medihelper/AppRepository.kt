package com.example.medihelper

import android.app.Application
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.medihelper.localdatabase.LocalDatabase
import com.example.medihelper.localdatabase.PlannedMedicineScheduler
import com.example.medihelper.localdatabase.dao.MedicineDAO
import com.example.medihelper.localdatabase.dao.MedicinePlanDAO
import com.example.medihelper.localdatabase.dao.MedicineTypeDAO
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.entities.MedicineTypeEntity
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object AppRepository {
    private val TAG = AppRepository::class.simpleName

    private lateinit var medicineDao: MedicineDAO
    private lateinit var medicineTypeDao: MedicineTypeDAO
    private lateinit var medicinePlanDao: MedicinePlanDAO
    private lateinit var plannedMedicineDao: com.example.medihelper.localdatabase.dao.PlannedMedicineDAO
    private var photosDir: File? = null

    private lateinit var medicineTypeEntityListLive: LiveData<List<MedicineTypeEntity>>

    fun init(app: Application) {
        Log.d(TAG, "init")
        val database = LocalDatabase.getInstance(app.applicationContext)
        medicineDao = database.medicineDao()
        medicineTypeDao = database.medicineTypeDao()
        medicinePlanDao = database.medicinePlanDao()
        plannedMedicineDao = database.plannedMedicineDao()

        initDatabaseData()
        photosDir = app.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        insertInitialMedicinesTypes()
    }

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
    fun deleteMedicine(medicineID: Int) = AsyncTask.execute {  medicineDao.delete(medicineID) }

    fun deleteMedicinePlan(medicinePlanID: Int) = AsyncTask.execute { medicinePlanDao.delete(medicinePlanID) }

    // Update
    fun updateMedicine(medicineEntity: MedicineEntity) = AsyncTask.execute {
        medicineDao.update(medicineEntity)
    }

    fun updatePlannedMedicine(plannedMedicineEntity: PlannedMedicineEntity) = AsyncTask.execute {
        plannedMedicineDao.update(plannedMedicineEntity)
    }

    fun getMedicineTypeListLive() = medicineTypeEntityListLive


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

    private fun initDatabaseData() {
        medicineTypeEntityListLive = medicineTypeDao.getAllLive()
    }

    private fun insertInitialMedicinesTypes() = AsyncTask.execute {
            val medicineTypesList = medicineTypeDao.getAll()
            if (medicineTypesList.isNullOrEmpty()) {
                val namesArray = arrayOf("pigułki", "ml", "g", "mg", "krople")
                namesArray.forEach { name ->
                    medicineTypeDao.insertSingle(MedicineTypeEntity(typeName = name))
                }
            }
        }
}