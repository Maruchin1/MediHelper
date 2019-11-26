package com.maruchin.medihelper.domain.deviceapi

import androidx.lifecycle.MutableLiveData
import java.io.File

interface CameraApi {
    fun capturePhoto(capturedFileLive: MutableLiveData<File>)
}