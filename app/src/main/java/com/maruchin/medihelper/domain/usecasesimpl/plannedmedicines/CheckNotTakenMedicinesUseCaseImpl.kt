package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException
import com.maruchin.medihelper.domain.usecases.plannedmedicines.CheckNotTakenMedicinesUseCase

class CheckNotTakenMedicinesUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo,
    private val deviceCalendar: DeviceCalendar,
    private val reminder: DeviceReminder
) : CheckNotTakenMedicinesUseCase {

    override suspend fun execute() {
        val currDate = deviceCalendar.getCurrDate()
        val currTime = deviceCalendar.getCurrTime()
        val notTakenPlannedMedicines = plannedMedicineRepo.getListNotTakenForDay(currDate, currTime)
        notTakenPlannedMedicines.forEach { plannedMedicine ->
            notifyAboutNotTakenPlannedMedicine(plannedMedicine)
        }
    }

    private suspend fun notifyAboutNotTakenPlannedMedicine(plannedMedicine: PlannedMedicine) {
        val medicine = getMedicine(plannedMedicine.medicineId)
        val profile = getProfile(plannedMedicine.profileId)
        val notifData = PlannedMedicineNotifData(plannedMedicine, profile, medicine)
        reminder.launchNotTakenMedicineNotification(notifData)
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }

    private suspend fun getProfile(profileId: String): Profile {
        return profileRepo.getById(profileId) ?: throw ProfileNotFoundException()
    }
}