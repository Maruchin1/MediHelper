package com.maruchin.medihelper.device.calendar

import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import java.util.*

class DeviceCalendarImpl : DeviceCalendar {

    override fun getCurrDate(): AppDate {
        return AppDate(getCurrTimeInMillis())
    }

    override fun getCurrTime(): AppTime {
        return AppTime(getCurrTimeInMillis())
    }

    private fun getCurrTimeInMillis() = Calendar.getInstance().timeInMillis
}