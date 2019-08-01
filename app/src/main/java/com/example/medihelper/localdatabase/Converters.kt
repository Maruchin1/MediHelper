package com.example.medihelper.localdatabase

import androidx.room.TypeConverter
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {

    @TypeConverter
    fun longTimeToDate(time: Long?): Date? {
        return time?.let { Date(time) }
    }

    @TypeConverter
    fun dateToLongTime(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun scheduleTypeToString(type: ScheduledMedicine.ScheduleType): String {
        return type.toString()
    }

    @TypeConverter
    fun stringToScheduleType(string: String): ScheduledMedicine.ScheduleType {
        return ScheduledMedicine.ScheduleType.valueOf(string)
    }

    @TypeConverter
    fun scheduleDaysToString(scheduleDays: ScheduledMedicine.ScheduleDays): String {
        return scheduleDays.toString()
    }

    @TypeConverter
    fun stringToScheduleDays(string: String): ScheduledMedicine.ScheduleDays {
        return ScheduledMedicine.ScheduleDays.valueOf(string)
    }

    @TypeConverter
    fun doseHourListToString(list: List<ScheduledMedicine.TimeOfTaking>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToDoseHourlist(string: String): List<ScheduledMedicine.TimeOfTaking> {
        val listType = object : TypeToken<List<ScheduledMedicine.TimeOfTaking>>() {}.type
        return Gson().fromJson(string, listType)
    }
}