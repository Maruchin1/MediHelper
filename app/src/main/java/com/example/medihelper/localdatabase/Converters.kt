package com.example.medihelper.localdatabase

import androidx.room.TypeConverter
import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import com.example.medihelper.localdatabase.entity.DeletedEntity
import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    // Date
    @TypeConverter
    fun appDateToString(date: AppDate?): String? {
        return date?.jsonFormatString
    }

    @TypeConverter
    fun stringToAppDate(string: String?): AppDate? {
        return string?.let { AppDate(string) }
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
    fun statusOfTakingToString(statusOfTaking: PlannedMedicineEntity.StatusOfTaking): String {
        return statusOfTaking.toString()
    }

    @TypeConverter
    fun stringToStatusOfTaking(string: String): PlannedMedicineEntity.StatusOfTaking {
        return PlannedMedicineEntity.StatusOfTaking.valueOf(string)
    }

    // EntityType
    @TypeConverter
    fun entityTypeToString(entityType: DeletedEntity.EntityType): String {
        return entityType.toString()
    }

    @TypeConverter
    fun stringToEntityType(string: String): DeletedEntity.EntityType {
        return DeletedEntity.EntityType.valueOf(string)
    }
}