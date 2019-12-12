package com.maruchin.medihelper.domain.usecases.datetime

import com.maruchin.medihelper.domain.deviceapi.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppExpireDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CalcDaysRemainUseCase(
    private val deviceCalendar: DeviceCalendar
) {
    suspend fun execute(expireDate: AppExpireDate): Int = withContext(Dispatchers.Default) {
        val currTimeMillis = deviceCalendar.getCurrTimeInMillis()

        val days1 = currTimeMillis / (24 * 3600 * 1000)
        val days2 = expireDate.timeInMillis / (24 * 3600 * 1000)
        val daysDiff = days2 - days1

        return@withContext daysDiff.toInt()
    }
}