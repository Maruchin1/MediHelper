package com.example.medihelper.localdatabase.entities

import androidx.room.*
import java.sql.Time
import java.util.*

@Entity(
    tableName = "planned_medicines",
    foreignKeys = [ForeignKey(
        entity = MedicinePlan::class,
        parentColumns = arrayOf("medicine_plan_id"),
        childColumns = arrayOf("medicine_plan_id"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["medicine_plan_id"])]
)
data class PlannedMedicine(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineID: Int = 0,

    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanID: Int,

    @ColumnInfo(name = "planned_date")
    var plannedDate: Date,

    @ColumnInfo(name = "planned_time")
    var plannedTime: Time,

    @ColumnInfo(name = "planned_dose_size")
    var plannedDoseSize: Int,

    @ColumnInfo(name = "status_of_taking")
    var statusOfTaking: StatusOfTaking = StatusOfTaking.WAITING
) {
    enum class StatusOfTaking {
        WAITING, TAKEN, NOT_TAKEN
    }
}