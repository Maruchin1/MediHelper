package com.example.medihelper.domain.usecases

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.MedicinePlan
import com.example.medihelper.domain.entities.MedicinePlanInputData
import com.example.medihelper.domain.entities.MedicinePlanWithMedicine
import com.example.medihelper.domain.repositories.MedicinePlanRepo

class MedicinePlanUseCases(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val plannedMedicineUseCases: PlannedMedicineUseCases
) {

    suspend fun addNewMedicinePlan(inputData: MedicinePlanInputData) {
        val newMedicinePlan = MedicinePlan(
            medicinePlanId = 0,
            medicineId = inputData.medicineId,
            personId = inputData.personId,
            durationType = inputData.durationType,
            startDate = inputData.startDate,
            endDate = inputData.endDate,
            daysType = inputData.daysType,
            daysOfWeek = inputData.daysOfWeek,
            intervalOfDays = inputData.intervalOfDays,
            timeDoseList = inputData.timeDoseList
        )
        val insertedId = medicinePlanRepo.insert(newMedicinePlan)
        val insertedPlan = medicinePlanRepo.getById(insertedId)
        plannedMedicineUseCases.addForMedicinePlan(insertedPlan)
    }

    suspend fun updateMedicinePlan(id: Int, inputData: MedicinePlanInputData) {
        val existingMedicinePlan = medicinePlanRepo.getById(id)
        val updatedMedicinePlan = existingMedicinePlan.copy(
            medicineId = inputData.medicineId,
            personId = inputData.personId,
            durationType = inputData.durationType,
            startDate = inputData.startDate,
            endDate = inputData.endDate,
            daysType = inputData.daysType,
            daysOfWeek = inputData.daysOfWeek,
            intervalOfDays = inputData.intervalOfDays,
            timeDoseList = inputData.timeDoseList
        )
        medicinePlanRepo.update(updatedMedicinePlan)
        plannedMedicineUseCases.updateForMedicinePlan(updatedMedicinePlan)
    }

    suspend fun deleteMedicinePlanById(id: Int) = medicinePlanRepo.deleteById(id)

    suspend fun getMedicinePlanById(id: Int): MedicinePlan = medicinePlanRepo.getById(id)

    fun getMedicinePlanWithMedicineAndPlannedMedicinesLiveById(id: Int) =
        medicinePlanRepo.getWithMedicineAndPlannedMedicinesLiveById(id)

    fun getMedicinePlanWithMedicineListLiveByPersonId(id: Int): LiveData<List<MedicinePlanWithMedicine>> =
        medicinePlanRepo.getWithMedicineListLiveByPersonId(id)
}