package com.example.medihelper.localdatabase.pojo

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.example.medihelper.localdatabase.AppDate
import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.example.medihelper.localdatabase.entity.TimeDoseEntity

data class MedicinePlanEditData(
    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanID: Int = 0,

    @ColumnInfo(name = "medicine_id")
    var medicineID: Int,

    @ColumnInfo(name = "person_id")
    var personID: Int,

    @ColumnInfo(name = "start_date")
    var startDate: AppDate,

    @ColumnInfo(name = "end_date")
    var endDate: AppDate? = null,

    @ColumnInfo(name = "schedule_type")
    var durationType: MedicinePlanEntity.DurationType,

    @Embedded
    var daysOfWeek: MedicinePlanEntity.DaysOfWeek? = null,

    @ColumnInfo(name = "interval_of_days")
    var intervalOfDays: Int? = null,

    @ColumnInfo(name = "schedule_days")
    var daysType: MedicinePlanEntity.DaysType? = null,

    @Relation(
        entity = TimeDoseEntity::class,
        parentColumn = "medicine_plan_id",
        entityColumn = "medicine_plan_id"
    )
    val timeDoseList: List<TimeDoseEditData>
)