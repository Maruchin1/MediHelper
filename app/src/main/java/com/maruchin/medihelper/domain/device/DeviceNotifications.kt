package com.maruchin.medihelper.domain.device

import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData

interface DeviceNotifications {

    suspend fun setupPlannedMedicinesChecking()
    suspend fun launchNotTakenMedicineNotification(data: PlannedMedicineNotifData)
    suspend fun launchPlannedMedicineNotification(data: PlannedMedicineNotifData)
    suspend fun schedulePlannedMedicineNotification(data: PlannedMedicineNotifData)
}