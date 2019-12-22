package com.maruchin.medihelper.domain.usecases.mediplans

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.model.MedicinePlanItem
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLiveMedicinesPlansItemsByProfileUseCase(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(profileId: String): LiveData<List<MedicinePlanItem>> = withContext(Dispatchers.Default) {
        val allMedicinesPlansLive = medicinePlanRepo.getAllListLive()
        return@withContext Transformations.switchMap(allMedicinesPlansLive) { list ->
            liveData {
                withContext(Dispatchers.Default) {
                    val itemsList = list.filter { medicinePlan ->
                        medicinePlan.profileId == profileId
                    }.map { medicinePlan ->
                        val medicine = medicineRepo.getById(medicinePlan.medicineId)?: throw Exception("Medicine doesn't exists")
                        MedicinePlanItem(medicinePlan, medicine)
                    }
                    emit(itemsList)
                }
            }
        }
    }
}