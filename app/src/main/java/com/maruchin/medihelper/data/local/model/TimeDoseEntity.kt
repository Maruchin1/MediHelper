package com.maruchin.medihelper.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.TimeDose

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
    val timeDoseId: Int,

    @ColumnInfo( name = "time")
    var time: AppTime,

    @ColumnInfo(name = "dose_size")
    var doseSize: Float,

    @ColumnInfo(name = "medicine_plan_id")
    var medicinePlanId: Int
) {
    constructor(timeDose: TimeDose, medicinePlanId: Int) : this(
        timeDoseId = 0,
        time = timeDose.time,
        doseSize = timeDose.doseSize,
        medicinePlanId = medicinePlanId
    )

    fun toTimeDose() = TimeDose(time, doseSize)
}