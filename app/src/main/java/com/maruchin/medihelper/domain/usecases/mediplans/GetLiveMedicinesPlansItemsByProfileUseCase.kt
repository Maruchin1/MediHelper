package com.maruchin.medihelper.domain.usecases.mediplans

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.model.MedicinePlanItem
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo

class GetLiveMedicinesPlansItemsByProfileUseCase(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(profileId: String): LiveData<List<MedicinePlanItem>> {
        val medicinesPlansLive = medicinePlanRepo.getListLiveByProfile(profileId)
        return Transformations.switchMap(medicinesPlansLive) { list ->
            liveData {
                val itemsList = list.map { medicinePlan ->
                    val medicine = medicineRepo.getById(medicinePlan.medicineId)?: throw Exception("Medicine doesn't exists")
                    MedicinePlanItem(medicinePlan, medicine)
                }
                emit(itemsList)
            }
        }
    }
}