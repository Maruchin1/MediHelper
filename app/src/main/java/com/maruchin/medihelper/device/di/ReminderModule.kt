package com.maruchin.medihelper.device.di

import com.maruchin.medihelper.device.notifications.DeviceNotificationsImpl
import com.maruchin.medihelper.domain.device.DeviceNotifications
import org.koin.dsl.module

val notificationsModule = module {
    single {
        DeviceNotificationsImpl(
            context = get(),
            settingsRepo = get()
        ) as DeviceNotifications
    }
}