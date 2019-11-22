package com.example.medihelper.device.di

import com.example.medihelper.device.camera.CameraPermission
import org.koin.dsl.module

val cameraModule = module {
    single {
        CameraPermission(
            context = get()
        )
    }
}