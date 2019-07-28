package com.example.medihelper.localdatabase

import androidx.room.TypeConverter
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
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
}