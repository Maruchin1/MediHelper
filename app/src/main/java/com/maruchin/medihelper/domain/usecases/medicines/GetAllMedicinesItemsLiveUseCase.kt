package com.maruchin.medihelper.domain.usecases.medicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.presentation.framework.map

class GetAllMedicinesItemsLiveUseCase(
    private val medicineRepo: MedicineRepo
) {

    suspend fun execute(): LiveData<List<MedicineItem>> {
        val allEntitiesLive = medicineRepo.getAllListLive()
        return Transformations.map(allEntitiesLive) { list ->
            list.map { mapMedicineToMedicineItem(it) }
        }
    }

    private fun mapMedicineToMedicineItem(medicine: Medicine) = MedicineItem(
        medicineId = medicine.medicineId,
        name = medicine.name,
        unit = medicine.unit,
        stateDate = medicine.getStateData()
    )

    data class MedicineItem(
        val medicineId: String,
        val name: String,
        val unit: String,
        val stateDate: MedicineStateData
    )
}