package com.maruchin.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.framework.BaseRepo

interface PlannedMedicineRepo : BaseRepo<PlannedMedicine> {

    suspend fun addNewList(entityList: List<PlannedMedicine>)
    suspend fun deleteListById(entityIdList: List<String>)
    suspend fun getListByMedicinePlan(medicinePlanId: String): List<PlannedMedicine>
    suspend fun getLiveListByProfileAndDate(profileId: String, date: AppDate): LiveData<List<PlannedMedicine>>
}