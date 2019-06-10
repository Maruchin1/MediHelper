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

    var date: Date,

    var time: String,

    @ColumnInfo(name = "dose_size")
    var doseSize: Int
)