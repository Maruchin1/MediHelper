package com.maruchin.medihelper.device.calendar

import com.maruchin.medihelper.domain.deviceapi.DeviceCalendar
import java.util.*

class DeviceCalendarImpl : DeviceCalendar {

    override fun getCurrTimeInMillis(): Long {
        return Calendar.getInstance().timeInMillis
    }
}