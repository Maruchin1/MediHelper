package com.maruchin.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.framework.BaseRepo

interface MedicinePlanRepo : BaseRepo<MedicinePlan> {
    suspend fun deleteListById(ids: List<String>)
    suspend fun getListByMedicine(medicineId: String): List<MedicinePlan>
    suspend fun getListByProfile(profileId: String): List<MedicinePlan>
    suspend fun getListLiveByProfile(profileId: String): LiveData<List<MedicinePlan>>
}