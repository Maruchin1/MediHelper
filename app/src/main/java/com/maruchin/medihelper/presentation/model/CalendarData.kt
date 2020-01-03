package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.AppDate
import java.util.*

data class CalendarData(
    val daysCount: Int,
    val startCalendar: Calendar,
    val endCalendar: Calendar,
    val initialCalendar: Calendar,
    val initialPosition: Int
) {
    companion object {

        fun get(daysOffset: Int, currDate: AppDate): CalendarData {
           return CalendarData(
               daysCount = calculateDaysCount(daysOffset),
               startCalendar = calculateStartCalendar(daysOffset, currDate),
               endCalendar = calculateEndCalendar(daysOffset, currDate),
               initialCalendar = calculateInitialCalendar(currDate),
               initialPosition = daysOffset
           )
        }

        private fun calculateDaysCount(daysOffset: Int): Int {
            return (daysOffset * 2) + 1
        }

        private fun calculateStartCalendar(daysOffset: Int, currDate: AppDate): Calendar {
            return Calendar.getInstance().apply {
                timeInMillis = currDate.timeInMillis
                add(Calendar.DATE, -daysOffset)
            }
        }

        private fun calculateEndCalendar(daysOffset: Int, currDate: AppDate): Calendar {
            return Calendar.getInstance().apply {
                timeInMillis = currDate.timeInMillis
                add(Calendar.DATE, daysOffset)
            }
        }

        private fun calculateInitialCalendar(currDate: AppDate): Calendar {
            return Calendar.getInstance().apply {
                timeInMillis = currDate.timeInMillis
            }
        }
    }
}