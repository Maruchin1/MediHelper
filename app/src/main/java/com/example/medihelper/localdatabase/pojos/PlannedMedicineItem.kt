package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import com.example.medihelper.AppTime
import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity

data class PlannedMedicineItem(
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineID: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "planned_dose_size")
    val plannedDoseSize: Float,

    @ColumnInfo(name = "planned_time")
    val plannedTime: AppTime,

    @ColumnInfo(name = "status_of_taking")
    val statusOfTaking: PlannedMedicineEntity.StatusOfTaking
)