package com.example.medihelper.localdatabase.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.*
import java.sql.Time
import java.util.*
import java.util.concurrent.TimeUnit

@Entity(
    tableName = "scheduled_medicines",
    foreignKeys = [ForeignKey(
        entity = Medicine::class,
        parentColumns = arrayOf("medicine_id"),
        childColumns = arrayOf("medicine_id")
    )],
    indices = [Index(value = ["medicine_id"])]
)
data class ScheduledMedicine(
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
    var scheduleDays: ScheduleDays,

    @ColumnInfo(name = "dose_hour")
    var timeOfTakingList: List<TimeOfTaking>
) {
    fun isScheduledForDate(date: Date): Boolean {
        return if (date.before(startDate)) {
            false
        } else {
            when (scheduleType) {
                ScheduleType.ONCE -> checkDateForOnce(date)
                ScheduleType.PERIOD -> checkDateForPeriod(date)
                ScheduleType.CONTINUOUS -> checkDateForContinuous(date)
            }
        }
    }

    private fun checkDateForOnce(date: Date) = date == startDate

    private fun checkDateForPeriod(date: Date): Boolean {
        return if (date.after(endDate)) {
            false
        } else {
            when (scheduleDays) {
                ScheduleDays.EVERYDAY -> true
                ScheduleDays.DAYS_OF_WEEK -> checkDateForDaysOfWeek(date)
                ScheduleDays.INTERVAL_OF_DAYS -> checkDateForIntervalOfDays(date)
            }
        }
    }

    private fun checkDateForContinuous(date: Date): Boolean {
        return when (scheduleDays) {
            ScheduleDays.EVERYDAY -> true
            ScheduleDays.DAYS_OF_WEEK -> checkDateForDaysOfWeek(date)
            ScheduleDays.INTERVAL_OF_DAYS -> checkDateForIntervalOfDays(date)
        }
    }

    private fun checkDateForDaysOfWeek(date: Date): Boolean {
        val calendar = Calendar.getInstance().apply {
            time = date
        }
        val dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK)
        return daysOfWeek!!.isDaySelectedByNumber(dayOfWeekNumber)
    }

    private fun checkDateForIntervalOfDays(date: Date): Boolean {
        val timeDiff = date.time - startDate.time
        val daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS)
        return daysDiff.rem(intervalOfDays!!.toLong()) == 0L
    }

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

        @Ignore
        private val daysNumbersMap = mapOf(
            1 to monday,
            2 to tuesday,
            3 to wednesday,
            4 to thursday,
            5 to friday,
            6 to saturday,
            7 to sunday
        )

        fun isDaySelectedByNumber(numberOfDay: Int): Boolean {
            return daysNumbersMap[numberOfDay] ?: throw Exception("Incorrect number of day")
        }
    }

    data class TimeOfTaking(
        var doseSize: Int = 1,
        var time: Time = Time(8, 0, 0)
    )
}