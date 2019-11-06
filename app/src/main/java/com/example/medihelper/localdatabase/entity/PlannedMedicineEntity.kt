package com.example.medihelper.localdatabase.entity

import androidx.room.*
import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import com.example.medihelper.R

@Entity(
    tableName = "planned_medicines",
    foreignKeys = [ForeignKey(
        entity = MedicinePlanEntity::class,
        parentColumns = arrayOf("medicine_plan_id"),
        childColumns = arrayOf("medicine_plan_id"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["medicine_plan_id"])]
)
data class PlannedMedicineEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineID: Int = 0,

    @ColumnInfo(name = "planned_medicine_remote_id")
    var plannedMedicineRemoteID: Long? = null,

    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanID: Int,

    @ColumnInfo(name = "planned_date")
    var plannedDate: AppDate,

    @ColumnInfo(name = "planned_time")
    var plannedTime: AppTime,

    @ColumnInfo(name = "planned_dose_size")
    var plannedDoseSize: Float,

    @ColumnInfo(name = "status_of_taking")
    var statusOfTaking: StatusOfTaking = StatusOfTaking.WAITING,

    @ColumnInfo(name = "synchronized_with_server")
    var synchronizedWithServer: Boolean = false
)

{
    enum class StatusOfTaking(val string: String, val colorResID: Int, val iconResID: Int) {
        WAITING("oczekujący", R.color.colorDarkerGray, R.drawable.round_radio_button_unchecked_24),
        TAKEN("przyjęty", R.color.colorStateGood, R.drawable.round_check_circle_24),
        NOT_TAKEN("nieprzyjęty", R.color.colorStateSmall, R.drawable.round_error_24)
    }
}