package com.maruchin.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.framework.BaseRepo

interface MedicinePlanRepo : BaseRepo<MedicinePlan> {
    suspend fun getListLiveByProfile(profileId: String): LiveData<List<MedicinePlan>>
}