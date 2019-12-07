package com.maruchin.medihelper.domain.usecases.medicines

import androidx.lifecycle.MutableLiveData
import com.maruchin.medihelper.domain.deviceapi.DeviceCamera
import java.io.File

class CaptureMedicinePhotoUseCase(
    private val deviceCamera: DeviceCamera
) {
    suspend fun execute(capturedFileLive: MutableLiveData<File>) {
        return deviceCamera.capturePhoto(capturedFileLive)
    }
}