package com.example.medihelper.device.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.medihelper.MainApplication

class CameraPermission(private val context: Context) {

    private val mainApp: MainApplication by lazy { context.applicationContext as MainApplication }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    fun isGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun askForCameraPermission(): Int {
        return mainApp.currActivity?.let { currActivity ->
            ActivityCompat.requestPermissions(
                currActivity,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )
             PERMISSION_REQUEST_CODE
        } ?: throw Exception("No currActivity available")
    }
}