package com.example.medihelper.localdatabase

import androidx.room.TypeConverter
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

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
    fun durationTypeToString(durationType: ScheduledMedicine.DurationType): String {
        return durationType.toString()
    }

    @TypeConverter
    fun stringToDurationType(string: String): ScheduledMedicine.DurationType {
        return ScheduledMedicine.DurationType.valueOf(string)
    }

    // DaysType
    @TypeConverter
    fun daysTypeToString(daysType: ScheduledMedicine.DaysType): String {
        return daysType.toString()
    }

    @TypeConverter
    fun stringToDaysType(string: String): ScheduledMedicine.DaysType {
        return ScheduledMedicine.DaysType.valueOf(string)
    }

    // List<TimeOfTaking>
    @TypeConverter
    fun timeOfTakingListToString(list: List<ScheduledMedicine.TimeOfTaking>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToTimeOfTakingList(string: String): List<ScheduledMedicine.TimeOfTaking> {
        val listType = object : TypeToken<List<ScheduledMedicine.TimeOfTaking>>() {}.type
        return Gson().fromJson(string, listType)
    }

    // List<SavedStatus>
    @TypeConverter
    fun takenMedicineArrayListToString(arrayList: ArrayList<ScheduledMedicine.TakenMedicine>): String {
        return Gson().toJson(arrayList)
    }

    @TypeConverter
    fun stringTotakenMedicineArrayList(string: String): ArrayList<ScheduledMedicine.TakenMedicine> {
        val listType = object : TypeToken<ArrayList<ScheduledMedicine.TakenMedicine>>() {}.type
        return Gson().fromJson(string, listType)
    }
}