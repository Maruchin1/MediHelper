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
    private val externalPicturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    fun getImageFile(imageName: String) = File(internalFilesDir, imageName)

    fun getTempImageCaptureIntent(capturedFileLive: MutableLiveData<File>): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(context.packageManager)?.also {
                val tempImageFile = createTempImageFile()
                capturedFileLive.postValue(tempImageFile)
                val photoURI = FileProvider.getUriForFile(
                    context,
                    "com.example.medihelper.fileprovider",
                    tempImageFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
        }
    }

    fun saveTempImageFileAsPerma(medicineName: String, tempFile: File): String {
        val fileName = medicineName + tempFile.name.replace("TMP", "")
        val file = File(internalFilesDir, fileName)
        tempFile.copyTo(file)
        return fileName
    }

    private fun createTempImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile("TMP_${timeStamp}", ".jpg", externalPicturesDir).apply {
            deleteOnExit()
        }
    }
}