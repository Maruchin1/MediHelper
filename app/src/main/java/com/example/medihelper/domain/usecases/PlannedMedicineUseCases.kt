package com.example.medihelper.domain.usecases

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicine

class PlannedMedicineUseCases {

    suspend fun updateAllStatus() {

    }

    fun getPlannedMedicineWithMedicineLiveById(id: Int): LiveData<PlannedMedicineWithMedicine> {
        TODO()
    }

    fun getPlannedMedicineWithMedicineListLiveByDateAndPerson(
        date: AppDate,
        personId: Int
    ): LiveData<List<PlannedMedicineWithMedicine>> {
        TODO()
    }

    fun changeMedicineTaken(id: Int, taken: Boolean) {
        
    }
}