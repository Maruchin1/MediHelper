package com.maruchin.medihelper.domain.deviceapi

import androidx.lifecycle.MutableLiveData
import java.io.File

interface DeviceCamera {
    fun capturePhoto(capturedFileLive: MutableLiveData<File>)
}