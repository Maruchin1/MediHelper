package com.example.medihelper

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun getCurrDate(): Date = Calendar.getInstance().time

    fun dateToString(date: Date): String = dateFormat().format(date)

    fun stringToDate(string: String): Date = dateFormat().parse(string)

    fun makeDate(day: Int, month: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }

    fun daysBetween(date1: Date, date2: Date): Int {
        return ((date2.time - date1.time) / (1000 * 60 * 60 * 24)).toInt() + 1
    }

//    fun dayAndMonthName(date: Date): String {
//        val todayCalendar = Calendar.getInstance()
//        val yesterdayCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
//        val tomorrowCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
//        val calendar = Calendar.getInstance().apply { time = date }
//        return when {
//            calendarsEqual(calendar, todayCalendar) -> TODAY
//            calendarsEqual(calendar, yesterdayCalendar) -> YEASTERDAY
//            calendarsEqual(calendar, tomorrowCalendar) -> TOMORROW
//            else -> calendar.run {
//                val day = get(Calendar.DAY_OF_MONTH)
//                val month = get(Calendar.MONTH)
//                val monthName = monthNameMap[month]
//                "$day $monthName"
//            }
//        }
//    }
//
//    fun weekDayName(date: Date): String {
//        Calendar.getInstance().run {
//            time = date
//            val weekDay = get(Calendar.DAY_OF_WEEK)
//            return "${weekDayNameMap[weekDay]}"
//        }
//    }
//
//    private fun calendarsEqual(calendar1: Calendar, calendar2: Calendar): Boolean {
//        val day1 = calendar1.get(Calendar.DAY_OF_MONTH)
//        val month1 = calendar1.get(Calendar.MONTH)
//        val year1 = calendar1.get(Calendar.YEAR)
//
//        val day2 = calendar2.get(Calendar.DAY_OF_MONTH)
//        val month2 = calendar2.get(Calendar.MONTH)
//        val year2 = calendar2.get(Calendar.YEAR)
//
//        return year1 == year2 && month1 == month2 && day1 == day2
//    }

    private fun dateFormat() = SimpleDateFormat.getDateInstance()

    const val DATE_PATTERN = "dd-MM-yyyy"
    const val TODAY = "Dzisiaj"
    const val YEASTERDAY = "Wczoraj"
    const val TOMORROW = "Jutro"

    private val monthNameMap = hashMapOf(
        0 to "sty",
        1 to "lut",
        2 to "mar",
        3 to "kwi",
        4 to "maj",
        5 to "cze",
        6 to "lip",
        7 to "sie",
        8 to "wrz",
        9 to "paź",
        10 to "lis",
        11 to "gru"
    )

    private val weekDayNameMap = hashMapOf(
        1 to "Niedziela",
        2 to "Poniedziałek",
        3 to "Wtorek",
        4 to "Środa",
        5 to "Czwartek",
        6 to "Piątek",
        7 to "Sobota"
    )
}