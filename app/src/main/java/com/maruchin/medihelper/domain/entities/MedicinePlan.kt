package com.maruchin.medihelper.domain.entities

abstract class MedicinePlan {
    abstract val medicinePlanId: String
    abstract val medicineId: String
    abstract val profileId: String
    abstract val timeDoseList: List<TimeDose>
}