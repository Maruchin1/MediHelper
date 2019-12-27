package com.maruchin.medihelper.domain.device

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime

interface DeviceCalendar {
    fun getCurrDate(): AppDate
    fun getCurrTime(): AppTime
}