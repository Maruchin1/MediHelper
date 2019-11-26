package com.maruchin.medihelper.device.di

import com.maruchin.medihelper.device.deviceapi.CalendarApiImpl
import com.maruchin.medihelper.device.deviceapi.CameraApiImpl
import com.maruchin.medihelper.device.deviceapi.NotificationApiImpl
import com.maruchin.medihelper.domain.deviceapi.CalendarApi
import com.maruchin.medihelper.domain.deviceapi.CameraApi
import com.maruchin.medihelper.domain.deviceapi.NotificationApi
import org.koin.dsl.module

val deviceApiModule = module {
    single {
        CalendarApiImpl() as CalendarApi
    }
    single {
        CameraApiImpl(
            context = get(),
            cameraPermission = get()
        ) as CameraApi
    }
    single {
        NotificationApiImpl(
            reminderWorkManager = get()
        ) as NotificationApi
    }
}