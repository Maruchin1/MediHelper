package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.framework.BaseEntityRepo

interface ProfileRepo : BaseEntityRepo<Profile> {
    suspend fun getMainId(): String?
    suspend fun getListByMedicine(medicineId: String): List<Profile>
    suspend fun getColorsList(): List<String>
}