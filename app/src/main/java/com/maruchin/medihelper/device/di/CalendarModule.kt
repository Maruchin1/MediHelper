package com.maruchin.medihelper.device.di

import com.maruchin.medihelper.device.calendar.DeviceCalendarImpl
import com.maruchin.medihelper.domain.deviceapi.DeviceCalendar
import org.koin.dsl.module

val calendarModule = module {
    single {
        DeviceCalendarImpl() as DeviceCalendar
    }
}