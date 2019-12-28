package com.maruchin.medihelper.device.di

import com.maruchin.medihelper.device.ringtone.DeviceRingtoneImpl
import com.maruchin.medihelper.domain.device.DeviceRingtone
import org.koin.dsl.module

val ringtoneModule = module {
    single {
        DeviceRingtoneImpl(
            context = get()
        ) as DeviceRingtone
    }
}