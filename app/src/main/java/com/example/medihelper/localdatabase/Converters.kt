package com.example.medihelper.localdatabase

import androidx.room.TypeConverter
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
}