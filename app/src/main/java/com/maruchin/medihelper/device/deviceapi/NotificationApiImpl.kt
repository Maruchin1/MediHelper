package com.maruchin.medihelper.device.deviceapi

import com.maruchin.medihelper.device.notifications.ReminderWorkManager
import com.maruchin.medihelper.domain.deviceapi.NotificationApi

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