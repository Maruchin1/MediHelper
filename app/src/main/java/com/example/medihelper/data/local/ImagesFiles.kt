package com.example.medihelper.data.local

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImagesFiles(private val context: Context) {

    private val internalFilesDir = context.filesDir

    fun getImageFile(imageName: String) = File(internalFilesDir, imageName)

    fun isTempFile(image: File): Boolean {
        val imageName = image.name
        return imageName.contains("TMP")
    }

    fun saveTempImageFileAsPerma(medicineName: String, tempFile: File): String {
        val fileName = medicineName + tempFile.name.replace("TMP", "")
        val file = File(internalFilesDir, fileName)
        tempFile.copyTo(file)
        return fileName
    }
}