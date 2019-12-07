package com.maruchin.medihelper.device.di

import com.maruchin.medihelper.device.camera.DeviceCameraImpl
import com.maruchin.medihelper.device.camera.CameraPermission
import com.maruchin.medihelper.domain.deviceapi.DeviceCamera
import org.koin.dsl.module

val cameraModule = module {
    single {
        CameraPermission(
            context = get()
        )
    }
    single {
        DeviceCameraImpl(
            context = get(),
            cameraPermission = get()
        ) as DeviceCamera
    }
}