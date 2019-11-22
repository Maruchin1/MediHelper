package com.example.medihelper.domain.utils

import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime
import com.example.medihelper.domain.entities.StatusOfTaking

class StatusOfTakingCalculator {

    fun getByTaken(
        plannedDate: AppDate,
        plannedTime: AppTime,
        currDate: AppDate,
        currTime: AppTime,
        taken: Boolean
    ) = when {
        taken -> StatusOfTaking.TAKEN
        else -> getWhenNotTaken(plannedDate, plannedTime, currDate, currTime)
    }

    fun getByCurrStatus(
        plannedDate: AppDate,
        plannedTime: AppTime,
        currDate: AppDate,
        currTime: AppTime,
        currStatusOfTaking: StatusOfTaking
    ) = when (currStatusOfTaking) {
        StatusOfTaking.TAKEN -> StatusOfTaking.TAKEN
        else -> getWhenNotTaken(plannedDate, plannedTime, currDate, currTime)
    }

    private fun getWhenNotTaken(
        plannedDate: AppDate,
        plannedTime: AppTime,
        currDate: AppDate,
        currTime: AppTime
    ) = when {
        plannedDate > currDate -> StatusOfTaking.WAITING
        plannedDate < currDate -> StatusOfTaking.NOT_TAKEN
        else -> when {
            plannedTime < currTime -> StatusOfTaking.NOT_TAKEN
            else -> StatusOfTaking.WAITING
        }
    }
}