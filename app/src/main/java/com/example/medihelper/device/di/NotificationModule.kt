package com.example.medihelper.device.di

import com.example.medihelper.device.notifications.MedicineReminderNotif
import com.example.medihelper.device.notifications.ReminderManager
import org.koin.dsl.module

val notificationModule = module {
    single {
        ReminderManager(
            context = get()
        )
    }
    single {
        MedicineReminderNotif(
            context = get()
        )
    }
}