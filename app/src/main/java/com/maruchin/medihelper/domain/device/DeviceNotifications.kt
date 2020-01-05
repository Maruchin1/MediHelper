package com.maruchin.medihelper.domain.device

import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData

interface DeviceNotifications {

    suspend fun setupNotTakenMedicinesChecking()
    suspend fun launchPlannedMedicineNotification(data: PlannedMedicineNotifData)
}