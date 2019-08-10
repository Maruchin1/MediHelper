package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import java.sql.Time
import java.util.*

data class PlannedMedicineDetails(
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineID: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "photo_file_path")
    var photoFilePath: String? = null,

    @ColumnInfo(name = "planned_dose_size")
    val plannedDoseSize: Int,

    @ColumnInfo(name = "planned_date")
    var plannedDate: Date,

    @ColumnInfo(name = "planned_time")
    val plannedTime: Time,

    @ColumnInfo(name = "status_of_taking")
    val statusOfTaking: PlannedMedicineEntity.StatusOfTaking
)