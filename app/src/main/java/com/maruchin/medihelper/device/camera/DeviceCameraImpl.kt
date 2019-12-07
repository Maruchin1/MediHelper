package com.maruchin.medihelper.device.camera

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.maruchin.medihelper.MainApplication
import com.maruchin.medihelper.domain.deviceapi.DeviceCamera
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DeviceCameraImpl(
    private val context: Context,
    private val cameraPermission: CameraPermission
) : DeviceCamera {

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
                    "com.maruchin.medihelper.fileprovider",
                    tempImageFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
        }
    }

    private fun getTempImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile(timeStamp, ".jpg", externalPicturesDir).apply {
            deleteOnExit()
        }
    }
}