package com.maruchin.medihelper.device.di

import com.maruchin.medihelper.device.camera.CameraPermission
import com.maruchin.medihelper.device.camera.DeviceCamera
import org.koin.dsl.module

val cameraModule = module {
    single {
        CameraPermission(
            context = get()
        )
    }
    single {
        DeviceCamera(
            context = get()
        )
    }
}