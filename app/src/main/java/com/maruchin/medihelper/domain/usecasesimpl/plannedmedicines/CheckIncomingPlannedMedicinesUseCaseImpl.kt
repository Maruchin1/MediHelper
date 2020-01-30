package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException
import com.maruchin.medihelper.domain.usecases.plannedmedicines.CheckIncomingPlannedMedicinesUseCase

class CheckIncomingPlannedMedicinesUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo,
    private val reminder: DeviceReminder
) : CheckIncomingPlannedMedicinesUseCase {

    companion object {
        private const val CHECK_MINUTES_RANGE = 90
    }

    override suspend fun execute() {
        val notTakenPlannedMedicines = plannedMedicineRepo.getListNotTakenForNextMinutes(CHECK_MINUTES_RANGE)
        notTakenPlannedMedicines.forEach { plannedMedicine ->
            scheduleNotification(plannedMedicine)
        }
    }

    private suspend fun scheduleNotification(plannedMedicine: PlannedMedicine) {
        val medicine = medicineRepo.getById(plannedMedicine.medicineId) ?: throw MedicineNotFoundException()
        val profile = profileRepo.getById(plannedMedicine.profileId) ?: throw ProfileNotFoundException()
        val notifData = PlannedMedicineNotifData(plannedMedicine, profile, medicine)
        reminder.schedulePlannedMedicineReminder(notifData)
    }
}