package com.example.medihelper.domain.usecases

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicine
import com.example.medihelper.domain.repositories.PlannedMedicineRepo
import com.example.medihelper.domain.utils.StatusOfTakingCalculator

class PlannedMedicineUseCases(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val statusOfTakingCalculator: StatusOfTakingCalculator,
    private val dateTimeUseCases: DateTimeUseCases
) {

    suspend fun updateAllStatus() {
        val updatedList = plannedMedicineRepo.getAllList().map { plannedMedicine ->
            plannedMedicine.apply {
                val newStatus = statusOfTakingCalculator.getByCurrStatus(
                    plannedDate = plannedMedicine.plannedDate,
                    plannedTime = plannedMedicine.plannedTime,
                    currDate = dateTimeUseCases.getCurrDate(),
                    currTime = dateTimeUseCases.getCurrTime(),
                    currStatusOfTaking = plannedMedicine.statusOfTaking
                )
                plannedMedicine.statusOfTaking = newStatus
            }
        }
        plannedMedicineRepo.update(updatedList)
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