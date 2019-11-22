package com.example.medihelper.presentation.model


data class MedicinePlanFormError(
    var globalError: String?,
    var startDateError: String?,
    var endDateError: String?
)