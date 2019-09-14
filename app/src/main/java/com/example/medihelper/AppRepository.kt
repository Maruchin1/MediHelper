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
import androidx.lifecycle.Transformations
import com.example.medihelper.localdatabase.AppDatabase
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.repositoriesimpl.MedicineDao
import com.example.medihelper.localdatabase.repositoriesimpl.MedicinePlanDao
import com.example.medihelper.localdatabase.repositoriesimpl.PersonDao
import com.example.medihelper.localdatabase.repositoriesimpl.PlannedMedicineDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object AppRepository {
    private val TAG = AppRepository::class.simpleName

    private var photosDir: File? = null

    fun init(app: Application) {
        Log.d(TAG, "init")
        photosDir = app.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }

    // Other
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
}