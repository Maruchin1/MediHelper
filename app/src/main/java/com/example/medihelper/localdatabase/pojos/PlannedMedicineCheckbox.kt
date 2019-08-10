package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import java.sql.Time
import java.util.*

data class PlannedMedicineCheckbox(
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineID: Int,

    @ColumnInfo(name = "planned_date")
    val plannedDate: Date,

    @ColumnInfo(name = "planned_time")
    val plannedTime: Time,

    @ColumnInfo(name = "status_of_taking")
    val statusOfTaking: PlannedMedicineEntity.StatusOfTaking
)