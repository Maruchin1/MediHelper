package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import java.sql.Time

data class PlannedMedicineItem(
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineID: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "type_name")
    val typeName: String,

    @ColumnInfo(name = "planned_dose_size")
    val plannedDoseSize: Int,

    @ColumnInfo(name = "planned_time")
    val plannedTime: Time,

    @ColumnInfo(name = "status_of_taking")
    val statusOfTaking: PlannedMedicineEntity.StatusOfTaking
)