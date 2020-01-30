package com.maruchin.medihelper.device.di

import com.maruchin.medihelper.device.update.DeviceUpdatesImpl
import com.maruchin.medihelper.domain.device.DeviceUpdates
import org.koin.dsl.module

val updatesModule = module {
    single {
        DeviceUpdatesImpl(
            context = get()
        ) as DeviceUpdates
    }
}