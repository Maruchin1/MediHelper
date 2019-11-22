package com.example.medihelper.device.deviceapi

import com.example.medihelper.domain.deviceapi.CalendarApi
import java.util.*

class CalendarApiImpl : CalendarApi {

    override fun getCurrTimeInMillis(): Long {
        return Calendar.getInstance().timeInMillis
    }
}