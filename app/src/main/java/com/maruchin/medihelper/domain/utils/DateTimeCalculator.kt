package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppExpireDate

class DateTimeCalculator {

    fun calcDaysDiff(date1: AppDate, date2: AppExpireDate): Int {
        val days1 = date1.timeInMillis / (24 * 3600 * 1000)
        val days2 = date2.timeInMillis / (24 * 3600 * 1000)
        val daysDiff = days2 - days1

        return daysDiff.toInt()
    }
}