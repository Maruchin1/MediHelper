package com.example.medihelper.device.deviceapi

import com.example.medihelper.device.notifications.ReminderWorkManager
import com.example.medihelper.domain.deviceapi.NotificationApi

class NotificationApiImpl(
    private val reminderWorkManager: ReminderWorkManager
) : NotificationApi {

    override fun updatePlannedMedicinesNotifications() {
        reminderWorkManager.updateReminders()
    }

    override fun enablePeriodicRemindersUpdate() {
        reminderWorkManager.enablePeriodicRemindersUpdate()
    }
}