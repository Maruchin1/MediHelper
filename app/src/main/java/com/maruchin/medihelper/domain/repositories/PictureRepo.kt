package com.maruchin.medihelper.domain.repositories

import java.io.File

interface PictureRepo {

    suspend fun saveMedicinePicture(pictureFile: File)
    suspend fun deleteMedicinePicture(pictureName: String)
}