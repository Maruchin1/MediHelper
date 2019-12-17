package com.maruchin.medihelper.domain.usecases.medicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo

class GetLiveAllMedicinesItemsUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(): LiveData<List<MedicineItem>> {
        val allEntitiesLive = medicineRepo.getAllListLive()
        return Transformations.switchMap(allEntitiesLive) { list ->
            liveData {
                val itemsList = list.map { MedicineItem(it) }
                emit(itemsList)
            }
        }
    }
}