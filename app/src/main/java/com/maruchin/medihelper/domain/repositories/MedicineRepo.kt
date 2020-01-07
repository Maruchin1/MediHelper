package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.framework.BaseEntityRepo

interface MedicineRepo : BaseEntityRepo<Medicine> {

    suspend fun searchForMedicineInfo(medicineName: String): List<MedicineInfoSearchResult>
    suspend fun getMedicineInfo(urlString: String): List<MedicineInfo>
}