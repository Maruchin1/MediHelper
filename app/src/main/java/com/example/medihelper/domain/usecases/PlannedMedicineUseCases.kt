package com.example.medihelper.domain.usecases

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.deviceapi.NotificationApi
import com.example.medihelper.domain.entities.*
import com.example.medihelper.domain.repositories.PlannedMedicineRepo
import com.example.medihelper.domain.utils.MedicineScheduler
import com.example.medihelper.domain.utils.StatusOfTakingCalculator

class PlannedMedicineUseCases(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val statusOfTakingCalculator: StatusOfTakingCalculator,
    private val dateTimeUseCases: DateTimeUseCases,
    private val medicineScheduler: MedicineScheduler,
    private val notificationApi: NotificationApi
) {

    suspend fun addForMedicinePlan(medicinePlan: MedicinePlan) {
        val plannedMedicineList = medicineScheduler.getPlannedMedicinesForMedicinePlan(medicinePlan)
        plannedMedicineRepo.insert(plannedMedicineList)
        notificationApi.updatePlannedMedicinesNotifications()
    }

    suspend fun updateForMedicinePlan(medicinePlan: MedicinePlan) {
        val currDate = dateTimeUseCases.getCurrDate()
        val medicinePlanFromNow = medicinePlan.copy(startDate = currDate)
        val plannedMedicineList = medicineScheduler.getPlannedMedicinesForMedicinePlan(medicinePlanFromNow)

        plannedMedicineRepo.deleteFromDateByMedicinePlanId(currDate, medicinePlan.medicinePlanId)
        plannedMedicineRepo.insert(plannedMedicineList)
        notificationApi.updatePlannedMedicinesNotifications()
    }

    suspend fun updateAllStatus() {
        val allPlannedMedicines = plannedMedicineRepo.getAllList()
        val updatedList = mutableListOf<PlannedMedicine>()
        allPlannedMedicines.forEach { plannedMedicine ->
            val newStatus = statusOfTakingCalculator.getByCurrStatus(
                plannedDate = plannedMedicine.plannedDate,
                plannedTime = plannedMedicine.plannedTime,
                currDate = dateTimeUseCases.getCurrDate(),
                currTime = dateTimeUseCases.getCurrTime(),
                currStatusOfTaking = plannedMedicine.statusOfTaking
            )
            if (newStatus != plannedMedicine.statusOfTaking) {
                updatedList.add(plannedMedicine.copy(statusOfTaking = newStatus))
            }
        }
        plannedMedicineRepo.update(updatedList)
    }

    suspend fun getPlannedMedicineWithMedicineAndPersonById(id: Int): PlannedMedicineWithMedicineAndPerson {
        return plannedMedicineRepo.getWithMedicineAndPersonById(id)
    }

    fun getPlannedMedicineWithMedicineLiveById(id: Int): LiveData<PlannedMedicineWithMedicine> {
        return plannedMedicineRepo.getWithMedicineLiveById(id)
    }

    fun getPlannedMedicineWithMedicineListLiveByDateAndPerson(
        date: AppDate,
        personId: Int
    ): LiveData<List<PlannedMedicineWithMedicine>> {
        return plannedMedicineRepo.getWithMedicineListLiveByDateAndPerson(date, personId)
    }

    suspend fun changeMedicineTaken(id: Int, taken: Boolean) {
        val plannedMedicine = plannedMedicineRepo.getById(id)
        val newStatus = statusOfTakingCalculator.getByTaken(
            plannedDate = plannedMedicine.plannedDate,
            plannedTime = plannedMedicine.plannedTime,
            currDate = dateTimeUseCases.getCurrDate(),
            currTime = dateTimeUseCases.getCurrTime(),
            taken = taken
        )
        plannedMedicine.statusOfTaking = newStatus
        plannedMedicineRepo.update(plannedMedicine)
    }
}