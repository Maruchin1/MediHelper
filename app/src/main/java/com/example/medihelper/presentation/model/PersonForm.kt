package com.example.medihelper.presentation.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

data class PersonForm(
    private var _name: String?,
    private var _colorId: Int
) : BaseObservable() {

    var name: String?
        @Bindable get() = _name
        set(value) {
            _name = value
            notifyPropertyChanged(BR.name)
        }

    var colorId: Int
        @Bindable get() = _colorId
        set(value) {
            _colorId = value
            notifyPropertyChanged(BR.colorId)
        }
}