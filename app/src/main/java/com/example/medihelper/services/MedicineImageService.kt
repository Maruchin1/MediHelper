package com.example.medihelper.services

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MedicineImageService(
    private val appFilesDir: File,
    private val externalImagesDir: File?
    ) {

    fun createTempImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile(
            "TMP_${timeStamp}",
            ".jpg",
            externalImagesDir
        ).apply {
            deleteOnExit()
        }
    }

    fun saveTmpFile(medicineName: String, tmpFile: File): String {
        val fileName = medicineName + tmpFile.name.replace("TMP", "")
        val file = File(appFilesDir, fileName)
        tmpFile.copyTo(file)
        return fileName
    }
}