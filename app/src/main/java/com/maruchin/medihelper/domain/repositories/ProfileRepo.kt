package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.framework.BaseRepo

interface ProfileRepo : BaseRepo<Profile> {
    suspend fun getMainId(): String?
    suspend fun getListByMedicine(medicineId: String): List<Profile>
}