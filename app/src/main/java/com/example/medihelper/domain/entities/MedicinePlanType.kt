package com.example.medihelper.domain.entities

enum class MedicinePlanType(val title: String) {
    ENDED("Zakończone"),
    ONGOING("Trwajace");

    companion object {
        fun getType(
            durationType: DurationType,
            startDate: AppDate,
            endDate: AppDate?,
            currDate: AppDate
        ) = when(durationType) {
            DurationType.ONCE -> {
                when {
                    currDate > startDate -> ENDED
                    else -> ONGOING
                }
            }
            DurationType.PERIOD -> {
                when {
                    currDate > endDate!! -> ENDED
                    else -> ONGOING
                }
            }
            DurationType.CONTINUOUS -> ONGOING
        }
    }
}