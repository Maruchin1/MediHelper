package com.example.medihelper.domain.entities

data class MedicineStateData(
    val stateAvailable: Boolean,
    val stateWeight: Float?,
    val emptyWeight: Float?,
    val colorId: Int?,
    val text: String?,
    val numberText: String?
)