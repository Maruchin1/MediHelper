package com.maruchin.medihelper.device.di

import com.maruchin.medihelper.device.reminder.DeviceReminderImpl
import com.maruchin.medihelper.domain.device.DeviceReminder
import org.koin.dsl.module

val notificationsModule = module {
    single {
        DeviceReminderImpl(
            context = get(),
            areRemindersEnabledUseCase = get()
        ) as DeviceReminder
    }
}