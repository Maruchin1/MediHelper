package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.R

data class PlannedMedicine(
    val plannedMedicineId: String,
    val medicinePlanId: String,
    val profileId: String,
    val medicineId: String,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val plannedDoseSize: Float,
    var status: Status
) {
    enum class Status(
        val text: String,
        val colorResId: Int,
        val iconResId: Int
    ) {
        TAKEN("przyjęty", R.color.colorStateGood, R.drawable.round_check_circle_24),
        NOT_TAKEN("nieprzyjęty", R.color.colorStateSmall, R.drawable.round_error_24),
        PENDING("oczekujący", R.color.colorDarkerGray, R.drawable.round_radio_button_unchecked_24)
    }
}