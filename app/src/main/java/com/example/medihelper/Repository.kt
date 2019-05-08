package com.example.medihelper

import android.app.Application
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.medihelper.localdatabase.LocalDatabase
import com.example.medihelper.localdatabase.dao.MedicineDAO
import com.example.medihelper.localdatabase.dao.MedicineTypeDAO
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Repository {
    private val TAG = Repository::class.simpleName

    private lateinit var medicineDao: MedicineDAO
    private lateinit var medicineTypeDao: MedicineTypeDAO
    private var photosDir: File? = null

    private lateinit var medicinesLive: LiveData<List<Medicine>>
    private lateinit var medicineTypesLive: LiveData<List<MedicineType>>

    fun init(app: Application) {
        Log.d(TAG, "init")
        val database = LocalDatabase.getInstance(app.applicationContext)
        medicineDao = database.medicineDao()
        medicineTypeDao = database.medicineTypeDao()
        initDatabaseData()
        photosDir = app.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        insertInitialMedicinesTypes()
    }

    fun getMedicinesLive() = medicinesLive

    fun getMedicineTypesLive() = medicineTypesLive

    fun getMedicineByIdLive(medicineId: Int) = medicineDao.getByIdLive(medicineId)

    fun getMedicineTypeByIdLive(medicineTypeId: Int) = medicineTypeDao.getByIdLive(medicineTypeId)

    fun insertMedicine(medicine: Medicine) {
        Log.d(TAG, "insertMedicine")
        AsyncTask.execute { medicineDao.insertSingle(medicine) }
    }

    fun insertMedicineType(medicineType: MedicineType) {
        AsyncTask.execute { medicineTypeDao.insertSingle(medicineType) }
    }

    fun createTempPhotoFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File.createTempFile(
                "TMP_JPEG_${timeStamp}_",
                ".png",
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
        medicinesLive = medicineDao.getAllLive()
        medicineTypesLive = medicineTypeDao.getAllLive()
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