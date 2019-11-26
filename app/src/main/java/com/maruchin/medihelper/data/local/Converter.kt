package com.maruchin.medihelper.data.local

import androidx.room.TypeConverter
import com.maruchin.medihelper.domain.entities.*


class Converter {

    // Date
    @TypeConverter
    fun appDateToString(date: AppDate?): String? {
        return date?.jsonFormatString
    }

    @TypeConverter
    fun stringToAppDate(string: String?): AppDate? {
        return string?.let { AppDate(string) }
    }

    // ExpireDate
    @TypeConverter
    fun appExpireDateToString(expireDate: AppExpireDate?): String? {
        return expireDate?.jsonFormatString
    }

    @TypeConverter
    fun stringToExpireDate(string: String?): AppExpireDate? {
        return string?.let { AppExpireDate(it) }
    }

    // Time
    @TypeConverter
    fun appTimeToString(time: AppTime?): String? {
        return time?.jsonFormatString
    }

    @TypeConverter
    fun stringToAppTime(string: String?): AppTime? {
        return string?.let { AppTime(string) }
    }

    // DurationType
    @TypeConverter
    fun durationTypeToString(durationType: DurationType): String {
        return durationType.toString()
    }

    @TypeConverter
    fun stringToDurationType(string: String): DurationType {
        return DurationType.valueOf(string)
    }

    // DaysType
    @TypeConverter
    fun daysTypeToString(daysType: DaysType?): String? {
        return daysType?.toString()
    }

    @TypeConverter
    fun stringToDaysType(string: String?): DaysType? {
        return string?.let { DaysType.valueOf(it) }
    }

//    // List<TimeOfTaking>
//    @TypeConverter
//    fun timeOfTakingListToString(list: List<MedicinePlanEntity.TimeOfTaking>): String {
//        return Gson().toJson(list)
//    }
//
//    @TypeConverter
//    fun stringToTimeOfTakingList(string: String): List<MedicinePlanEntity.TimeOfTaking> {
//        val listType = object : TypeToken<List<MedicinePlanEntity.TimeOfTaking>>() {}.type
//        return Gson().fromJson(string, listType)
//    }

    // StatusOfTaking
    @TypeConverter
    fun statusOfTakingToString(statusOfTaking: StatusOfTaking): String {
        return statusOfTaking.toString()
    }

    @TypeConverter
    fun stringToStatusOfTaking(string: String): StatusOfTaking {
        return StatusOfTaking.valueOf(string)
    }
}