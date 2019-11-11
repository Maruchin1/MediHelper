package com.example.medihelper.localdata.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.localdata.type.AppTime

data class PlannedMedicineAlarmData(
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineId: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "person_name")
    val personName: String,

    @ColumnInfo(name = "person_color_res_id")
    var personColorResId: Int,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "planned_dose_size")
    val plannedDoseSize: Float,

    @ColumnInfo(name = "planned_time")
    val plannedTime: AppTime
)