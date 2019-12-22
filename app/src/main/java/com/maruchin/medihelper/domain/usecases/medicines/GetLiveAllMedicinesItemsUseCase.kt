package com.maruchin.medihelper.domain.usecases.medicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLiveAllMedicinesItemsUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(): LiveData<List<MedicineItem>> = withContext(Dispatchers.Default) {
        val allEntitiesLive = medicineRepo.getAllListLive()
        return@withContext Transformations.switchMap(allEntitiesLive) { list ->
            liveData {
                withContext(Dispatchers.Default) {
                    val itemsList = list.map { MedicineItem(it) }
                    emit(itemsList)
                }
            }
        }
    }
}