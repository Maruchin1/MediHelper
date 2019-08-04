package com.example.medihelper.localdatabase

import androidx.room.TypeConverter
import com.example.medihelper.localdatabase.entities.MedicinePlan
import com.example.medihelper.localdatabase.entities.PlannedMedicine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Time
import java.util.*

class Converters {

    // Date
    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun longToDate(long: Long?): Date? {
        return long?.let { Date(long) }
    }

    // Time
    @TypeConverter
    fun timeToLong(time: Time): Long {
        return time.time
    }

    @TypeConverter
    fun longToTime(long: Long): Time {
        return Time(long)
    }

    // DurationType
    @TypeConverter
    fun durationTypeToString(durationType: MedicinePlan.DurationType): String {
        return durationType.toString()
    }

    @TypeConverter
    fun stringToDurationType(string: String): MedicinePlan.DurationType {
        return MedicinePlan.DurationType.valueOf(string)
    }

    // DaysType
    @TypeConverter
    fun daysTypeToString(daysType: MedicinePlan.DaysType): String {
        return daysType.toString()
    }

    @TypeConverter
    fun stringToDaysType(string: String): MedicinePlan.DaysType {
        return MedicinePlan.DaysType.valueOf(string)
    }

    // List<TimeOfTaking>
    @TypeConverter
    fun timeOfTakingListToString(list: List<MedicinePlan.TimeOfTaking>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToTimeOfTakingList(string: String): List<MedicinePlan.TimeOfTaking> {
        val listType = object : TypeToken<List<MedicinePlan.TimeOfTaking>>() {}.type
        return Gson().fromJson(string, listType)
    }

    // StatusOfTaking
    @TypeConverter
    fun statusOfTakingToString(statusOfTaking: PlannedMedicine.StatusOfTaking): String {
        return statusOfTaking.toString()
    }

    @TypeConverter
    fun stringToStatusOfTaking(string: String): PlannedMedicine.StatusOfTaking {
        return PlannedMedicine.StatusOfTaking.valueOf(string)
    }

}