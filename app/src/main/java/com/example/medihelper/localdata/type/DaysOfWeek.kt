package com.example.medihelper.localdata.type

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName

class DaysOfWeek : BaseObservable() {
    @Bindable
    @SerializedName("monday")
    var monday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.monday)
        }

    @Bindable
    @SerializedName("tuesday")
    var tuesday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.tuesday)
        }

    @Bindable
    @SerializedName("wednesday")
    var wednesday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.wednesday)
        }

    @Bindable
    @SerializedName("thursday")
    var thursday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.thursday)
        }

    @Bindable
    @SerializedName("friday")
    var friday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.friday)
        }

    @Bindable
    @SerializedName("saturday")
    var saturday: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.saturday)
        }

    @Bindable
    @SerializedName("sunday")
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
        return "monday:$monday, tuesday:$tuesday, wednesday:$wednesday, thursday:$thursday, friday:$friday, saturday:$saturday, sunday:$sunday"
    }
}