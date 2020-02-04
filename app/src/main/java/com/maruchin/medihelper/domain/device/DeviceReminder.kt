package com.maruchin.medihelper.domain.device

import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData

interface DeviceReminder {

    suspend fun setupPlannedMedicinesReminders()

    suspend fun checkReminders()

    suspend fun schedulePlannedMedicineReminder(data: PlannedMedicineNotifData)

    suspend fun launchNotTakenMedicineNotification(data: PlannedMedicineNotifData)

    suspend fun launchPlannedMedicineNotification(data: PlannedMedicineNotifData)

    suspend fun launchPlannedMedicineAlarm(dataJson: String)
}