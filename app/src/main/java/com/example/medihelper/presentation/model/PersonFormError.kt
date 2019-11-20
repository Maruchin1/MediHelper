package com.example.medihelper.presentation.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

data class PersonFormError(
    private var _errorName: String?
) : BaseObservable() {

    var errorName: String?
        @Bindable get() = _errorName
        set(value) {
            _errorName = value
            notifyPropertyChanged(BR.errorName)
        }
}