package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.framework.BaseRepo
import java.io.File

interface MedicineRepo : BaseRepo<Medicine> {

    suspend fun saveMedicinePicture(pictureFile: File)
    suspend fun deleteMedicinePicture(pictureName: String)
    suspend fun getMedicineUnits(): List<String>
}