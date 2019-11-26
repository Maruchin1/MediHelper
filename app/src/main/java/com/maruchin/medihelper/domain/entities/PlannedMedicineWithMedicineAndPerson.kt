package com.maruchin.medihelper.domain.entities

data class PlannedMedicineWithMedicineAndPerson(
    val plannedMedicineId: Int,
    val medicine: Medicine,
    val person: Person,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val plannedDoseSize: Float,
    var statusOfTaking: StatusOfTaking
)