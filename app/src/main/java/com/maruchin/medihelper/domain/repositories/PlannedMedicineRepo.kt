package com.maruchin.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.framework.BaseEntityRepo

interface PlannedMedicineRepo : BaseEntityRepo<PlannedMedicine> {

    suspend fun addNewList(entityList: List<PlannedMedicine>): List<PlannedMedicine>

    suspend fun deleteListById(entityIdList: List<String>)

    suspend fun getListByMedicinePlan(medicinePlanId: String): List<PlannedMedicine>

    suspend fun getListByMedicinePlanBeforeDate(medicinePlanId: String, date: AppDate): List<PlannedMedicine>

    suspend fun getListNotTakenForDay(date: AppDate, time: AppTime): List<PlannedMedicine>

    suspend fun getListNotTakenForNextMinutes(minutes: Int): List<PlannedMedicine>

    suspend fun getLiveListByProfileAndDate(profileId: String, date: AppDate): LiveData<List<PlannedMedicine>>
}