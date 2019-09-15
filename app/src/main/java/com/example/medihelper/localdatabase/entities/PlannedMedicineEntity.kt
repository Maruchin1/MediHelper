package com.example.medihelper.localdatabase.entities

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

    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanID: Int,

    @ColumnInfo(name = "planned_date")
    var plannedDate: AppDate,

    @ColumnInfo(name = "planned_time")
    var plannedTime: AppTime,

    @ColumnInfo(name = "planned_dose_size")
    var plannedDoseSize: Float,

    @ColumnInfo(name = "status_of_taking")
    var statusOfTaking: StatusOfTaking = StatusOfTaking.WAITING
) {
    fun setMedicineTaken(taken: Boolean) {
        statusOfTaking = if (taken) {
            StatusOfTaking.TAKEN
        } else {
            statusOfTakingByCurrDate()
        }
    }

    fun updateStatusByCurrDate() {
        if (statusOfTaking != StatusOfTaking.TAKEN) {
            val newStatus = statusOfTakingByCurrDate()
            if (newStatus != statusOfTaking) {
                statusOfTaking = newStatus
            }
        }
    }

    private fun statusOfTakingByCurrDate(): StatusOfTaking {
        val currDate = AppDate.currDate()
        val currTime = AppTime.currTime()
        return when (AppDate.compareDates(currDate, plannedDate)) {
            2 -> StatusOfTaking.WAITING
            1 -> StatusOfTaking.NOT_TAKEN
            else -> when (AppTime.compareTimes(currTime, plannedTime)) {
                1 -> StatusOfTaking.NOT_TAKEN
                else -> StatusOfTaking.WAITING
            }
        }
    }

    enum class StatusOfTaking(val shortString: String, val colorResID: Int, val iconResID: Int?) {
        WAITING("oczekujący", R.color.colorDarkerGray, null),
        TAKEN("przyjęty", R.color.colorStateGood, R.drawable.baseline_check_24),
        NOT_TAKEN("nieprzyjęty", R.color.colorStateSmall, R.drawable.round_close_24)
    }
}