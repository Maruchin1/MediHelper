package com.example.medihelper.localdata.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.medihelper.localdata.type.AppTime

@Entity(
    tableName = "times_doses",
    foreignKeys = [
        ForeignKey(
            entity = MedicinePlanEntity::class,
            parentColumns = arrayOf("medicine_plan_id"),
            childColumns = arrayOf("medicine_plan_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class TimeDoseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "time_dose_id")
    val timeDoseId: Int = 0,

    @ColumnInfo( name = "time")
    var time: AppTime = AppTime(8, 0),

    @ColumnInfo(name = "dose_size")
    var doseSize: Float = 1.0f,

    @ColumnInfo(name = "medicine_plan_id")
    var medicinePlanId: Int
)