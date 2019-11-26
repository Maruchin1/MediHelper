package com.maruchin.medihelper.device.deviceapi

import com.maruchin.medihelper.domain.deviceapi.CalendarApi
import java.util.*

class CalendarApiImpl : CalendarApi {

    override fun getCurrTimeInMillis(): Long {
        return Calendar.getInstance().timeInMillis
    }
}