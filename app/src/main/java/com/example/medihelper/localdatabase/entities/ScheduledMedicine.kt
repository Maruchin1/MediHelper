package com.example.medihelper.localdatabase.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.*
import java.util.*

@Entity(
    tableName = "scheduled_medicines",
    foreignKeys = [ForeignKey(
        entity = Medicine::class,
        parentColumns = arrayOf("medicine_id"),
        childColumns = arrayOf("medicine_id")
    )],
    indices = [Index(value = ["medicine_id"])]
)
data class ScheduledMedicine (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "scheduled_medicine_id")
    val scheduledMedicineID: Int? = null,

    @ColumnInfo(name = "medicine_id")
    var medicineID: Int,

    @ColumnInfo(name = "start_date")
    var startDate: Date,

    @ColumnInfo(name = "end_date")
    var endDate: Date? = null,

    @ColumnInfo(name = "schedule_type")
    var scheduleType: ScheduleType,

    @Embedded
    var daysOfWeek: DaysOfWeek? = null,

    @ColumnInfo(name = "interval_of_days")
    var intervalOfDays: Int? = null,

    @ColumnInfo(name = "schedule_days")
    var scheduleDays: ScheduleDays
) {
    enum class ScheduleType {
        ONCE, PERIOD, CONTINUOUS
    }

    enum class ScheduleDays {
        EVERYDAY, DAYS_OF_WEEK, INTERVAL_OF_DAYS
    }

    class DaysOfWeek : BaseObservable() {
        @Bindable
        var monday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.monday)
        }

        @Bindable
        var tuesday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.tuesday)
        }

        @Bindable
        var wednesday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.wednesday)
        }

        @Bindable
        var thursday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.thursday)
        }

        @Bindable
        var friday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.friday)
        }

        @Bindable
        var saturday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.saturday)
        }

        @Bindable
        var sunday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.sunday)
        }
    }
}