package com.example.medihelper.domain.entities

import com.example.medihelper.R

enum class StatusOfTaking(val string: String, val colorResID: Int, val iconResID: Int) {
    WAITING("oczekujący", R.color.colorDarkerGray, R.drawable.round_radio_button_unchecked_24),
    TAKEN("przyjęty", R.color.colorStateGood, R.drawable.round_check_circle_24),
    NOT_TAKEN("nieprzyjęty", R.color.colorStateSmall, R.drawable.round_error_24);

}