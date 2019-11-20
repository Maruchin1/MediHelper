package com.example.medihelper.localdata.pojo

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.DaysType
import com.example.medihelper.domain.entities.DurationType
import com.example.medihelper.localdata.type.DaysOfWeek


data class MedicinePlanItem(
    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanId: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "duration_type")
    val durationType: DurationType,

    @ColumnInfo(name = "start_date")
    val startDate: AppDate,

    @ColumnInfo(name = "end_date")
    val endDate: AppDate? = null,

    @ColumnInfo(name = "days_type")
    val daysType: DaysType? = null,

    @Embedded
    val daysOfWeek: DaysOfWeek? = null,

    @ColumnInfo(name = "interval_of_days")
    val intervalOfDays: Int? = null
)