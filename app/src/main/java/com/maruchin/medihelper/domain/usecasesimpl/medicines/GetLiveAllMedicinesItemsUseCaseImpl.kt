package com.maruchin.medihelper.domain.usecasesimpl.medicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetLiveAllMedicinesItemsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLiveAllMedicinesItemsUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : GetLiveAllMedicinesItemsUseCase {

    override suspend fun execute(): LiveData<List<MedicineItem>> = withContext(Dispatchers.Default) {
        val allEntitiesLive = medicineRepo.getAllListLive()
        return@withContext Transformations.switchMap(allEntitiesLive) { list ->
            getLiveItemsFromMedicines(list)
        }
    }

    private fun getLiveItemsFromMedicines(medicines: List<Medicine>): LiveData<List<MedicineItem>> {
        return liveData {
            val items = mapMedicinesToItems(medicines)
            emit(items)
        }
    }

    private suspend fun mapMedicinesToItems(medicines: List<Medicine>): List<MedicineItem> = withContext(Dispatchers.Default) {
        return@withContext medicines.map { MedicineItem(it) }
    }
}