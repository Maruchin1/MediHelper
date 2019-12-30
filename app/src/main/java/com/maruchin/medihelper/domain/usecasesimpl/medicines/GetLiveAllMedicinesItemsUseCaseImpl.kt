package com.maruchin.medihelper.domain.usecasesimpl.medicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
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
            liveData {
                withContext(Dispatchers.Default) {
                    val itemsList = list.map { MedicineItem(it) }
                    emit(itemsList)
                }
            }
        }
    }
}