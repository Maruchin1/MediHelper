package com.example.medihelper.localdata.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.localdata.type.AppTime
import com.example.medihelper.localdata.entity.PlannedMedicineEntity
import com.example.medihelper.localdata.type.StatusOfTaking

data class PlannedMedicineItem(
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineId: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "planned_dose_size")
    val plannedDoseSize: Float,

    @ColumnInfo(name = "planned_time")
    val plannedTime: AppTime,

    @ColumnInfo(name = "status_of_taking")
    val statusOfTaking: StatusOfTaking
)