package com.example.medihelper.localdatabase.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.*
import com.example.medihelper.AppDateTimeUtil
import java.sql.Time
import java.util.*
import kotlin.text.StringBuilder

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
    val scheduledMedicineID: Int = 0,

    @ColumnInfo(name = "medicine_id")
    var medicineID: Int,

    @ColumnInfo(name = "start_date")
    var startDate: Date,

    @ColumnInfo(name = "end_date")
    var endDate: Date? = null,

    @ColumnInfo(name = "schedule_type")
    var durationType: DurationType,

    @Embedded
    var daysOfWeek: DaysOfWeek? = null,

    @ColumnInfo(name = "interval_of_days")
    var intervalOfDays: Int? = null,

    @ColumnInfo(name = "schedule_days")
    var daysType: DaysType,

    @ColumnInfo(name = "dose_hour")
    var timeOfTakingList: List<TimeOfTaking>
) {
    fun isScheduledForDate(date: Date): Boolean {
        val laterDate = AppDateTimeUtil.compareDates(startDate, date)
        return if (laterDate == 2 || laterDate == 0) {
            when (durationType) {
                DurationType.ONCE -> checkDateForOnce(date)
                DurationType.PERIOD -> checkDateForPeriod(date)
                DurationType.CONTINUOUS -> checkDateForContinuous(date)
            }
        } else {
            false
        }
    }

    private fun checkDateForOnce(date: Date) = AppDateTimeUtil.compareDates(startDate, date) == 0

    private fun checkDateForPeriod(date: Date): Boolean {
        return if (date.after(endDate)) {
            false
        } else {
            when (daysType) {
                DaysType.NONE -> true
                DaysType.EVERYDAY -> true
                DaysType.DAYS_OF_WEEK -> checkDateForDaysOfWeek(date)
                DaysType.INTERVAL_OF_DAYS -> checkDateForIntervalOfDays(date)
            }
        }
    }

    private fun checkDateForContinuous(date: Date): Boolean {
        return when (daysType) {
            DaysType.NONE -> true
            DaysType.EVERYDAY -> true
            DaysType.DAYS_OF_WEEK -> checkDateForDaysOfWeek(date)
            DaysType.INTERVAL_OF_DAYS -> checkDateForIntervalOfDays(date)
        }
    }

    private fun checkDateForDaysOfWeek(date: Date): Boolean {
        val calendar = Calendar.getInstance().apply {
            time = date
        }
        val dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK)
        val isDaySelected = daysOfWeek!!.isDaySelectedByNumber(dayOfWeekNumber)
        return isDaySelected
    }

    private fun checkDateForIntervalOfDays(date: Date): Boolean {
        val daysDiff = AppDateTimeUtil.daysBetween(startDate, date)
        val rem = daysDiff.rem(intervalOfDays!!)
        return rem == 0L
    }

    enum class DurationType {
        ONCE, PERIOD, CONTINUOUS
    }

    enum class DaysType {
        NONE, EVERYDAY, DAYS_OF_WEEK, INTERVAL_OF_DAYS
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

        fun isDaySelectedByNumber(numberOfDay: Int): Boolean {
            return when(numberOfDay) {
                2 -> monday
                3 -> tuesday
                4 -> wednesday
                5 -> thursday
                6 -> friday
                7 -> saturday
                1 -> sunday
                else -> throw Exception("Incorrect number of day")
            }
        }

        fun getSelectedDaysString(): String {
            return StringBuilder().run {
                if (monday) {
                    append("poniedziałek, ")
                }
                if (tuesday) {
                    append("wtorek, ")
                }
                if (wednesday) {
                    append("środa, ")
                }
                if (thursday) {
                    append("czwartek, ")
                }
                if (friday) {
                    append("piątek, ")
                }
                if (saturday) {
                    append("sobota, ")
                }
                if (sunday) {
                    append("niedziela, ")
                }
                setLength(length - 2)
                toString()
            }
        }

        override fun toString(): String {
            return "modnay:$monday, tuesday:$tuesday, wednesday:$wednesday, thursday:$thursday, friday:$friday, saturday:$saturday, sunday:$sunday"
        }
    }

    data class TimeOfTaking(
        var doseSize: Int = 1,
        var time: Time = Time(8, 0, 0)
    )
}