package com.example.medihelper.domain.entities

data class TimeDose(
    val timeDoseId: Int,
    val medicinePlanId: Int,
    val time: AppTime,
    val doseSize: Float
)