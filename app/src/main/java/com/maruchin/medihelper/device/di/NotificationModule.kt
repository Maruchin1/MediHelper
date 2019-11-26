package com.maruchin.medihelper.device.di

import com.maruchin.medihelper.device.notifications.MedicineReminderNotification
import com.maruchin.medihelper.device.notifications.ReminderWorkManager
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