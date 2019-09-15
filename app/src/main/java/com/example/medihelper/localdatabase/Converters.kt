package com.example.medihelper.localdatabase

import androidx.room.TypeConverter
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Date
import java.sql.Time


class Converters {

    // Date
    @TypeConverter
    fun appDateToLong(date: AppDate?): Long? {
        return date?.timeInMillis
    }

    @TypeConverter
    fun longToAppDate(long: Long?): AppDate? {
        return long?.let { AppDate(long) }
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
    fun durationTypeToString(durationType: MedicinePlanEntity.DurationType): String {
        return durationType.toString()
    }

    @TypeConverter
    fun stringToDurationType(string: String): MedicinePlanEntity.DurationType {
        return MedicinePlanEntity.DurationType.valueOf(string)
    }

    // DaysType
    @TypeConverter
    fun daysTypeToString(daysType: MedicinePlanEntity.DaysType): String {
        return daysType.toString()
    }

    @TypeConverter
    fun stringToDaysType(string: String): MedicinePlanEntity.DaysType {
        return MedicinePlanEntity.DaysType.valueOf(string)
    }

    // List<TimeOfTaking>
    @TypeConverter
    fun timeOfTakingListToString(list: List<MedicinePlanEntity.TimeOfTaking>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToTimeOfTakingList(string: String): List<MedicinePlanEntity.TimeOfTaking> {
        val listType = object : TypeToken<List<MedicinePlanEntity.TimeOfTaking>>() {}.type
        return Gson().fromJson(string, listType)
    }

    // StatusOfTaking
    @TypeConverter
    fun statusOfTakingToString(statusOfTaking: PlannedMedicineEntity.StatusOfTaking): String {
        return statusOfTaking.toString()
    }

    @TypeConverter
    fun stringToStatusOfTaking(string: String): PlannedMedicineEntity.StatusOfTaking {
        return PlannedMedicineEntity.StatusOfTaking.valueOf(string)
    }
}