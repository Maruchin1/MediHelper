package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.framework.BaseRepo

interface MedicineRepo : BaseRepo<Medicine> {

    suspend fun getMedicineUnits(): List<String>
}