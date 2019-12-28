package com.maruchin.medihelper.domain.device

import com.maruchin.medihelper.domain.entities.PlannedMedicine

interface DeviceReminder {

    fun addReminders(plannedMedicines: List<PlannedMedicine>)
    fun cancelReminders(plannedMedicines: List<PlannedMedicine>)
    fun updateReminder(plannedMedicine: PlannedMedicine)
    suspend fun launchReminderNotification(plannedMedicineId: String)
    fun launchReminderAlarm(plannedMedicineId: String)
}