package com.example.medihelper.localdata.pojo

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.data.local.model.TimeDoseEntity
import com.example.medihelper.domain.entities.DaysType
import com.example.medihelper.domain.entities.DurationType

data class MedicinePlanEditData(
    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanId: Int = 0,

    @ColumnInfo(name = "medicine_id")
    var medicineId: Int,

    @ColumnInfo(name = "person_id")
    var personId: Int,

    @ColumnInfo(name = "duration_type")
    var durationType: DurationType,

    @ColumnInfo(name = "start_date")
    var startDate: AppDate,

    @ColumnInfo(name = "end_date")
    var endDate: AppDate? = null,

    @ColumnInfo(name = "days_type")
    var daysType: DaysType? = null,

    @Embedded
    var daysOfWeek: DaysOfWeek? = null,

    @ColumnInfo(name = "interval_of_days")
    var intervalOfDays: Int? = null,

    @Relation(
        entity = TimeDoseEntity::class,
        parentColumn = "medicine_plan_id",
        entityColumn = "medicine_plan_id"
    )
    val timeDoseList: List<TimeDoseEditData>
)