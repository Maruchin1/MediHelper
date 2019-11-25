package com.example.medihelper.device.di

import com.example.medihelper.device.notifications.MedicineReminderNotification
import com.example.medihelper.device.notifications.ReminderWorkManager
import org.koin.dsl.module

val notificationModule = module {
    single {
        ReminderWorkManager(
            context = get()
        )
    }
    single {
        MedicineReminderNotification(
            context = get()
        )
    }
}