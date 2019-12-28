package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotifyAboutPlannedMedicineUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val deviceReminder: DeviceReminder,
    private val settingsRepo: SettingsRepo
) {
    suspend fun execute(plannedMedicineId: String) = withContext(Dispatchers.Default) {
        plannedMedicineRepo.getById(plannedMedicineId)?.let { plannedMedicine ->
            if (plannedMedicine.status == PlannedMedicine.Status.NOT_TAKEN) {
                notifyAboutPlannedMedicine(plannedMedicineId)
            }
        }
    }

    private suspend fun notifyAboutPlannedMedicine(plannedMedicineId: String) {
        when (settingsRepo.getReminderMode()) {
            ReminderMode.NOTIFICATION -> deviceReminder.launchReminderNotification(plannedMedicineId)
            ReminderMode.ALARM -> deviceReminder.launchReminderAlarm(plannedMedicineId)
        }
    }
}