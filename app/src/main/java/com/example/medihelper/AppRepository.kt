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
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicinePlan
import com.example.medihelper.localdatabase.entities.MedicineType
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

    private lateinit var medicineListLive: LiveData<List<Medicine>>
    private lateinit var medicineTypeListLive: LiveData<List<MedicineType>>
    private lateinit var medicinesPlanListLive: LiveData<List<MedicinePlan>>

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

    fun getMedicineListLive() = medicineListLive

    fun getMedicineTypeListLive() = medicineTypeListLive

    fun getMedicinesPlanListLive() = medicinesPlanListLive

    fun getMedicineByIdLive(medicineId: Int) = medicineDao.getByIdLive(medicineId)

    fun getMedicineTypeByIdLive(medicineTypeId: Int) = medicineTypeDao.getByIdLive(medicineTypeId)

    fun getPlannedMedicineListByDateLive(date: Date) = plannedMedicineDao.getByDateLive(date)

    fun deleteMedicine(medicine: Medicine) = AsyncTask.execute {
        medicineDao.delete(medicine)
    }

    fun deleteMedicinePlan(medicinePlan: MedicinePlan) = AsyncTask.execute {
        medicinePlanDao.delete(medicinePlan)
    }

    fun insertMedicine(medicine: Medicine) = AsyncTask.execute {
        medicineDao.insertSingle(medicine)
    }

    fun insertMedicineType(medicineType: MedicineType) = AsyncTask.execute {
        medicineTypeDao.insertSingle(medicineType)
    }

    fun insertMedicinePlan(medicinePlan: MedicinePlan) = AsyncTask.execute {
        val addedMedicinePlanID = medicinePlanDao.insert(medicinePlan)
        val addedMedicinePlan = medicinePlanDao.getById(addedMedicinePlanID.toInt())
        val plannedMedicineList = PlannedMedicineScheduler().getPlannedMedicineList(addedMedicinePlan)
        plannedMedicineDao.insert(plannedMedicineList)
    }

    fun updateMedicine(medicine: Medicine) = AsyncTask.execute {
        medicineDao.update(medicine)
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

    private fun initDatabaseData() {
        medicineListLive = medicineDao.getAllLive()
        medicineTypeListLive = medicineTypeDao.getAllLive()
        medicinesPlanListLive = medicinePlanDao.getAllLive()
    }

    private fun insertInitialMedicinesTypes() {
        val insertRunnable = Runnable {
            val medicineTypesList = medicineTypeDao.getAll()
            if (medicineTypesList.isNullOrEmpty()) {
                val namesArray = arrayOf("pigułki", "ml", "g", "mg", "krople")
                namesArray.forEach { name ->
                    insertMedicineType(MedicineType(typeName = name))
                }
            }
        }
        AsyncTask.execute(insertRunnable)
    }
}