package com.maruchin.medihelper.domain.usecases.plannedmedicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo

class GetLivePlannedMedicinesItemsByDateUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(profileId: String, date: AppDate): LiveData<List<PlannedMedicineItem>> {
        val plannedMedicinesLive = plannedMedicineRepo.getLiveListByProfileAndDate(profileId, date)
        return Transformations.switchMap(plannedMedicinesLive) { list ->
            liveData {
                val itemsList = list.map { plannedMedicine ->
                    val medicine = medicineRepo.getById(plannedMedicine.medicineId) ?: throw Exception("Medicine doesn't exist")
                    PlannedMedicineItem(plannedMedicine, medicine)
                }
                emit(itemsList)
            }
        }
    }
}