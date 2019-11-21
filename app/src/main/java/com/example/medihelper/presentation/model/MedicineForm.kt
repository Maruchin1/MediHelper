package com.example.medihelper.presentation.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.medihelper.domain.entities.AppExpireDate
import com.example.medihelper.domain.entities.Medicine
import com.example.medihelper.domain.entities.MedicineInputData
import java.io.File

data class MedicineForm(
    private var _name: String?,
    private var _unit: String,
    private var _expireDate: AppExpireDate?,
    private var _packageSize: Float?,
    private var _currState: Float?,
    private var _additionalInfo: String?,
    private var _image: File?
) : BaseObservable() {

    var name: String?
        @Bindable get() = _name
        set(value) {
            _name = value
            notifyPropertyChanged(BR.name)
        }

    var unit: String
        @Bindable get() = _unit
        set(value) {
            _unit = value
            notifyPropertyChanged(BR.unit)
        }

    var expireDate: AppExpireDate?
        @Bindable get() = _expireDate
        set(value) {
            _expireDate = value
            notifyPropertyChanged(BR.expireDate)
        }

    var packageSize: Float?
        @Bindable get() = _packageSize
        set(value) {
            _packageSize = value
            notifyPropertyChanged(BR.packageSize)
        }

    var currState: Float?
        @Bindable get() = _currState
        set(value) {
            _currState = value
            notifyPropertyChanged(BR.currState)
        }

    var additionalInfo: String?
        @Bindable get() = _additionalInfo
        set(value) {
            _additionalInfo = value
            notifyPropertyChanged(BR.additionalInfo)
        }

    var image: File?
        @Bindable get() = _image
        set(value) {
            _image = value
            notifyPropertyChanged(BR.image)
        }

    constructor(medicine: Medicine) : this(
        _name = medicine.name,
        _unit = medicine.unit,
        _expireDate = medicine.expireDate,
        _packageSize = medicine.packageSize,
        _currState = medicine.currState,
        _additionalInfo = medicine.additionalInfo,
        _image = medicine.image
    )

    fun toInputData() = MedicineInputData(
        name = name!!,
        unit = unit,
        expireDate = expireDate!!,
        packageSize = packageSize,
        currState = currState,
        additionalInfo = additionalInfo,
        image = image
    )
}