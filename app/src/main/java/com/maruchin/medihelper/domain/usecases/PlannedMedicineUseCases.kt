package com.maruchin.medihelper.domain.usecases

import com.maruchin.medihelper.domain.deviceapi.NotificationApi
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.utils.PlannedMedicineScheduler
import com.maruchin.medihelper.domain.utils.StatusOfTakingCalculator

class PlannedMedicineUseCases(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val statusOfTakingCalculator: StatusOfTakingCalculator,
    private val dateTimeUseCases: DateTimeUseCases,
    private val plannedMedicineScheduler: PlannedMedicineScheduler,
    private val notificationApi: NotificationApi
) {

//    suspend fun addForMedicinePlan(medicinePlan: MedicinePlan) {
//        val plannedMedicineList = medicineScheduler.getPlannedMedicinesForMedicinePlan(medicinePlan)
//        plannedMedicineRepo.insert(plannedMedicineList)
//        notificationApi.updatePlannedMedicinesNotifications()
//    }
//
//    suspend fun updateForMedicinePlan(medicinePlan: MedicinePlan) {
//        val currDate = dateTimeUseCases.getCurrDate()
//        val medicinePlanFromNow = medicinePlan.copy(startDate = currDate)
//        val plannedMedicineList = medicineScheduler.getPlannedMedicinesForMedicinePlan(medicinePlanFromNow)
//
//        plannedMedicineRepo.deleteFromDateByMedicinePlanId(currDate, medicinePlan.medicinePlanId)
//        plannedMedicineRepo.insert(plannedMedicineList)
//        notificationApi.updatePlannedMedicinesNotifications()
//    }
//
//    suspend fun updateAllStatus() {
//        val allPlannedMedicines = plannedMedicineRepo.getAllList()
//        val updatedList = mutableListOf<PlannedMedicine>()
//        allPlannedMedicines.forEach { plannedMedicine ->
//            val newStatus = statusOfTakingCalculator.getByCurrStatus(
//                plannedDate = plannedMedicine.plannedDate,
//                plannedTime = plannedMedicine.plannedTime,
//                currDate = dateTimeUseCases.getCurrDate(),
//                currTime = dateTimeUseCases.getCurrTime(),
//                currStatusOfTaking = plannedMedicine.statusOfTaking
//            )
//            if (newStatus != plannedMedicine.statusOfTaking) {
//                updatedList.add(plannedMedicine.copy(statusOfTaking = newStatus))
//            }
//        }
//        plannedMedicineRepo.update(updatedList)
//    }
//
//    suspend fun getPlannedMedicineWithMedicineAndPersonById(id: Int): PlannedMedicineWithMedicineAndPerson {
//        return plannedMedicineRepo.getWithMedicineAndPersonById(id)
//    }
//
//    fun getPlannedMedicineWithMedicineLiveById(id: Int): LiveData<PlannedMedicineWithMedicine> {
//        return plannedMedicineRepo.getWithMedicineLiveById(id)
//    }
//
//    fun getPlannedMedicineWithMedicineListLiveByDateAndPerson(
//        date: AppDate,
//        profileId: Int
//    ): LiveData<List<PlannedMedicineWithMedicine>> {
//        return plannedMedicineRepo.getWithMedicineListLiveByDateAndPerson(date, profileId)
//    }
//
//    suspend fun changeMedicineTaken(id: Int, taken: Boolean) {
//        val plannedMedicine = plannedMedicineRepo.getById(id)
//        val newStatus = statusOfTakingCalculator.getByTaken(
//            plannedDate = plannedMedicine.plannedDate,
//            plannedTime = plannedMedicine.plannedTime,
//            currDate = dateTimeUseCases.getCurrDate(),
//            currTime = dateTimeUseCases.getCurrTime(),
//            taken = taken
//        )
//        plannedMedicine.statusOfTaking = newStatus
//        plannedMedicineRepo.update(plannedMedicine)
//    }
}