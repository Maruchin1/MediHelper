package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.framework.BaseEntityRepo

//interface PlannedMedicineRepo : BaseEntityRepo<PlannedMedicine> {
//
//
//
//    suspend fun addNewList(entityList: List<PlannedMedicine>): List<PlannedMedicine>
//    suspend fun deleteListById(entityIdList: List<String>)
//    suspend fun getListByMedicinePlan(medicinePlanId: String): List<PlannedMedicine>
//    suspend fun getListNotTakenForLastMinutes(minutes: Int): List<PlannedMedicine>
//    suspend fun getLiveListByProfileAndDate(profileId: String, date: AppDate): LiveData<List<PlannedMedicine>>
//}