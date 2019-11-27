package com.maruchin.medihelper.domain.usecases.medicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo

class GetAllMedicinesItemsLiveUseCase(
    private val medicineRepo: MedicineRepo
) {

    suspend fun execute(): LiveData<List<MedicineItem>> {
        val allEntitiesLive = medicineRepo.getAllListLive()
        return Transformations.map(allEntitiesLive) { list ->
            list.map { MedicineItem(it) }
        }
    }
}