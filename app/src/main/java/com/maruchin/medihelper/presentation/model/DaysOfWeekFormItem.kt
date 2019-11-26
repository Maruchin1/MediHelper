package com.maruchin.medihelper.presentation.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.maruchin.medihelper.domain.entities.DaysOfWeek

data class DaysOfWeekFormItem(
    private var _monday: Boolean = false,
    private var _tuesday: Boolean = false,
    private var _wednesday: Boolean = false,
    private var _thursday: Boolean = false,
    private var _friday: Boolean = false,
    private var _saturday: Boolean = false,
    private var _sunday: Boolean = false
) : BaseObservable() {

    var monday: Boolean
        @Bindable get() = _monday
        set(value) {
            _monday = value
            notifyPropertyChanged(BR.monday)
        }

    var tuesday: Boolean
        @Bindable get() = _tuesday
        set(value) {
            _tuesday = value
            notifyPropertyChanged(BR.tuesday)
        }

    var wednesday: Boolean
        @Bindable get() = _wednesday
        set(value) {
            _wednesday = value
            notifyPropertyChanged(BR.wednesday)
        }

    var thursday: Boolean
        @Bindable get() = _thursday
        set(value) {
            _thursday = value
            notifyPropertyChanged(BR.thursday)
        }

    var friday: Boolean
        @Bindable get() = _friday
        set(value) {
            _friday = value
            notifyPropertyChanged(BR.friday)
        }

    var saturday: Boolean
        @Bindable get() = _saturday
        set(value) {
            _saturday = value
            notifyPropertyChanged(BR.saturday)
        }

    var sunday: Boolean
        @Bindable get() = _sunday
        set(value) {
            _sunday = value
            notifyPropertyChanged(BR.sunday)
        }

    constructor(daysOfWeek: DaysOfWeek) : this(
        daysOfWeek.monday,
        daysOfWeek.tuesday,
        daysOfWeek.wednesday,
        daysOfWeek.thursday,
        daysOfWeek.friday,
        daysOfWeek.saturday,
        daysOfWeek.sunday
    )

    fun toDaysOfWeek() = DaysOfWeek(monday, tuesday, wednesday, thursday, friday, saturday, sunday)

    fun isAnySelected(): Boolean {
        return arrayOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday).any { it }
    }
}