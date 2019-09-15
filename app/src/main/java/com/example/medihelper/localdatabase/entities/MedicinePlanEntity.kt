package com.example.medihelper.localdatabase.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.*
import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import java.sql.Date
import java.sql.Time

@Entity(
    tableName = "medicines_plans",
    foreignKeys = [
        ForeignKey(
            entity = MedicineEntity::class,
            parentColumns = arrayOf("medicine_id"),
            childColumns = arrayOf("medicine_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = arrayOf("person_id"),
            childColumns = arrayOf("person_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["medicine_id"])]
)
data class MedicinePlanEntity(
    @PrimaryKey(autoGenerate = true)
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
    var durationType: DurationType,

    @Embedded
    var daysOfWeek: DaysOfWeek? = null,

    @ColumnInfo(name = "interval_of_days")
    var intervalOfDays: Int? = null,

    @ColumnInfo(name = "schedule_days")
    var daysType: DaysType,

    @ColumnInfo(name = "dose_hour_list")
    var timeOfTakingList: List<TimeOfTaking>
) {
    enum class DurationType {
        ONCE, PERIOD, CONTINUOUS
    }

    enum class DaysType {
        NONE, EVERYDAY, DAYS_OF_WEEK, INTERVAL_OF_DAYS
    }

    data class TimeOfTaking(
        var doseSize: Float = 1.0f,
        var time: AppTime = AppTime(8, 0)
    )

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

        fun isDaySelected(numberOfDay: Int): Boolean {
            return when (numberOfDay) {
                1 -> sunday
                2 -> monday
                3 -> tuesday
                4 -> wednesday
                5 -> thursday
                6 -> friday
                7 -> saturday
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
}