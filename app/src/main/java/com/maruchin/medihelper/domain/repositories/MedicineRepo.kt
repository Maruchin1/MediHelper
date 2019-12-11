package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.framework.BaseRepo
import java.io.File

interface MedicineRepo : BaseRepo<Medicine> {

    suspend fun saveMedicinePicture(pictureFile: File)
    suspend fun deleteMedicinePicture(pictureName: String)
    suspend fun getMedicineUnits(): List<String>

    suspend fun searchForMedicineInfo(medicineName: String): List<MedicineInfoSearchResult>
    suspend fun getMedicineInfo(urlString: String): List<MedicineInfo>
}