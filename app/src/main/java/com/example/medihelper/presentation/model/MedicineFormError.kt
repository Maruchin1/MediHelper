package com.example.medihelper.presentation.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

data class MedicineFormError(
    private var _errorMedicineName: String?,
    private var _errorExpireDate: String?,
    private var _errorCurrState: String?
) : BaseObservable() {

    var errorMedicineName: String?
        @Bindable get() = _errorMedicineName
        set(value) {
            _errorMedicineName = value
            notifyPropertyChanged(BR.errorMedicineName)
        }

    var errorExpireDate: String?
        @Bindable get() = _errorExpireDate
        set(value) {
            _errorExpireDate = value
            notifyPropertyChanged(BR.errorExpireDate)
        }

    var errorCurrState: String?
        @Bindable get() = _errorCurrState
        set(value) {
            _errorCurrState = value
            notifyPropertyChanged(BR.errorCurrState)
        }
}