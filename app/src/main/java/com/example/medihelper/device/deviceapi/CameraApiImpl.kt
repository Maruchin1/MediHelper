package com.example.medihelper.device.deviceapi

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medihelper.MainApplication
import com.example.medihelper.device.camera.CameraPermission
import com.example.medihelper.domain.deviceapi.CameraApi
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraApiImpl(
    private val context: Context,
    private val cameraPermission: CameraPermission
) : CameraApi {

    private val externalPicturesDir: File by lazy { context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) }
    private val mainApp: MainApplication by lazy { context.applicationContext as MainApplication }

    override fun capturePhoto(capturedFileLive: MutableLiveData<File>) {
        if (cameraPermission.isGranted()) {
            val intent = getCapturePhotoIntent(capturedFileLive)
            mainApp.currActivity?.startActivity(intent)
        } else {
            cameraPermission.askForCameraPermission()
        }
    }

    private fun getCapturePhotoIntent(capturedFileLive: MutableLiveData<File>): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(context.packageManager)?.also {
                val tempImageFile = getTempImageFile()
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

    private fun getTempImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile("TMP_${timeStamp}", ".jpg", externalPicturesDir).apply {
            deleteOnExit()
        }
    }
}