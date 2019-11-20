package com.example.medihelper.presentation.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.medihelper.domain.entities.*

data class MedicinePlanForm(
    private var _durationType: DurationType,
    private var _startDate: AppDate,
    private var _endDate: AppDate?,
    private var _daysType: DaysType?,
    private var _daysOfWeek: DaysOfWeek?,
    private var _intervalOfDays: Int?
) : BaseObservable() {

    var durationType: DurationType
        @Bindable get() = _durationType
        set(value) {
            _durationType = value
            notifyPropertyChanged(BR.durationType)
        }

    var startDate: AppDate
        @Bindable get() = _startDate
        set(value) {
            _startDate = value
            notifyPropertyChanged(BR.startDate)
        }

    var endDate: AppDate?
        @Bindable get() = _endDate
        set(value) {
            _endDate = value
            notifyPropertyChanged(BR.endDate)
        }

    var daysType: DaysType?
        @Bindable get() = _daysType
        set(value) {
            _daysType = value
            notifyPropertyChanged(BR.daysType)
        }

    var daysOfWeek: DaysOfWeek?
        @Bindable get() = _daysOfWeek
        set(value) {
            _daysOfWeek = value
            notifyPropertyChanged(BR.daysOfWeek)
        }

    var intervalOfDays: Int?
        @Bindable get() = _intervalOfDays
        set(value) {
            _intervalOfDays = value
            notifyPropertyChanged(BR.intervalOfDays)
        }

    constructor(medicinePlan: MedicinePlan) : this(
        _durationType = medicinePlan.durationType,
        _startDate = medicinePlan.startDate,
        _endDate = medicinePlan.endDate,
        _daysType = medicinePlan.daysType,
        _daysOfWeek = medicinePlan.daysOfWeek,
        _intervalOfDays = medicinePlan.intervalOfDays
    )
}