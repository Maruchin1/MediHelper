package com.example.medihelper.device.deviceapi

import com.example.medihelper.device.notifications.ReminderManager
import com.example.medihelper.domain.deviceapi.NotificationApi

class NotificationApiImpl(
    private val reminderManager: ReminderManager
) : NotificationApi {

    override fun updatePlannedMedicinesNotifications() {
        reminderManager.updateReminders()
    }
}