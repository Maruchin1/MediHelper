package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetLivePlannedMedicinesItemsByDateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLivePlannedMedicinesItemsByDateUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo
) : GetLivePlannedMedicinesItemsByDateUseCase {

    override suspend fun execute(
        profileId: String,
        date: AppDate
    ): LiveData<List<PlannedMedicineItem>> = withContext(Dispatchers.Default) {
        val plannedMedicinesLive = plannedMedicineRepo.getLiveListByProfileAndDate(profileId, date)
        return@withContext Transformations.switchMap(plannedMedicinesLive) { plannedMedicines ->
            getLivePlannedMedicinesItems(plannedMedicines)
        }
    }

    private fun getLivePlannedMedicinesItems(plannedMedicines: List<PlannedMedicine>): LiveData<List<PlannedMedicineItem>> {
        return liveData {
            val items = mapPlannedMedicineToItems(plannedMedicines)
            emit(items)
        }
    }

    private suspend fun mapPlannedMedicineToItems(
        plannedMedicines: List<PlannedMedicine>
    ): List<PlannedMedicineItem> = withContext(Dispatchers.Default) {
        return@withContext plannedMedicines.map { plannedMedicine ->
            val medicine = getMedicine(plannedMedicine.medicineId)
            val item = PlannedMedicineItem(plannedMedicine, medicine)
            return@map item
        }
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }
}