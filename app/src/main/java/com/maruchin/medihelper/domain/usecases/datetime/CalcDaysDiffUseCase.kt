package com.maruchin.medihelper.domain.usecases.datetime

import com.maruchin.medihelper.domain.deviceapi.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CalcDaysDiffUseCase(
    private val deviceCalendar: DeviceCalendar
) {
    suspend fun execute(date: AppDate): Int = withContext(Dispatchers.Default) {
        val currTimeMillis = deviceCalendar.getCurrTimeInMillis()

        val days1 = currTimeMillis / (24 * 3600 * 1000)
        val days2 = date.timeInMillis / (24 * 3600 * 1000)
        val daysDiff = days2 - days1

        return@withContext daysDiff.toInt() + 1
    }
}