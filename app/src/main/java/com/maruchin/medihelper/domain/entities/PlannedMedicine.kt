package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.framework.BaseEntity

data class PlannedMedicine(
    override val entityId: String,
    val medicinePlanId: String,
    val profileId: String,
    val medicineId: String,
    val plannedDate: AppDate,
    var plannedTime: AppTime,
    val plannedDoseSize: Float,
    var status: Status
) : BaseEntity() {

    enum class Status(val text: String, val colorId: Int, val iconId: Int) {
        TAKEN("pryjęty", R.color.colorStateGood, R.drawable.round_check_circle_24),
        NOT_TAKEN("nieprzyjęty", R.color.colorDarkerGray, R.drawable.round_radio_button_unchecked_24)
    }
}