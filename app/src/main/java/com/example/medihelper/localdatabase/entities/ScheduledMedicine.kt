package com.example.medihelper.localdatabase.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "scheduled_medicines",
    foreignKeys = [ForeignKey(
        entity = Medicine::class,
        parentColumns = arrayOf("medicine_id"),
        childColumns = arrayOf("medicine_id")
    )],
    indices = [Index(value = ["medicine_id"])]
)
data class ScheduledMedicine (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "scheduled_medicine_id")
    val scheduledMedicineID: Int? = null,

    @ColumnInfo(name = "medicine_id")
    var medicineID: Int,

    var startDate: Date,

    var endDate: Date? = null,

    var scheduleType: ScheduleType,

    @Embedded
    var daysOfWeek: DaysOfWeek? = null,

    var intervalOfDays: Int? = null

//    var date: Date,
//
//    var time: String,
//
//    @ColumnInfo(name = "dose_size")
//    var doseSize: Int
) {
    enum class ScheduleType {
        ONCE, PERIOD, CONTINUOUS
    }

    class DaysOfWeek(
        var monday: Boolean = false,
        var tuesday: Boolean = false,
        var wednesday: Boolean = false,
        var thursday: Boolean = false,
        var friday: Boolean = false,
        var saturday: Boolean = false,
        var sunday: Boolean = false
    )
}