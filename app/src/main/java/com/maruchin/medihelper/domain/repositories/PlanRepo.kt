package com.maruchin.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.framework.BaseEntityRepo

interface PlanRepo : BaseEntityRepo<Plan> {
    suspend fun deleteListById(ids: List<String>)
    suspend fun getByMedicine(medicineId: String): List<Plan>
    suspend fun getByProfile(profileId: String): List<Plan>
    suspend fun getLiveByProfile(profileId: String): LiveData<List<Plan>>
}