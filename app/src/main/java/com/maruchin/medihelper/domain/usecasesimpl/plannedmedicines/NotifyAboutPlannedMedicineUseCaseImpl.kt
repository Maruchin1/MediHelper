package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.PlannedMedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.plannedmedicines.NotifyAboutPlannedMedicineUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotifyAboutPlannedMedicineUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val deviceReminder: DeviceReminder,
    private val settingsRepo: SettingsRepo
) : NotifyAboutPlannedMedicineUseCase {

    override suspend fun execute(plannedMedicineId: String) = withContext(Dispatchers.Default) {
        val plannedMedicine = getPlannedMedicine(plannedMedicineId)
        if (plannedMedicine.status == PlannedMedicine.Status.NOT_TAKEN) {
            notifyAboutPlannedMedicine(plannedMedicineId)
        }
        return@withContext
    }

    private suspend fun getPlannedMedicine(plannedMedicineId: String): PlannedMedicine {
        return plannedMedicineRepo.getById(plannedMedicineId) ?: throw PlannedMedicineNotFoundException()
    }

    private suspend fun notifyAboutPlannedMedicine(plannedMedicineId: String) {
        when (settingsRepo.getReminderMode()) {
            ReminderMode.NOTIFICATION -> deviceReminder.launchReminderNotification(plannedMedicineId)
            ReminderMode.ALARM -> deviceReminder.launchReminderAlarm(plannedMedicineId)
        }
    }
}