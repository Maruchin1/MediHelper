package com.maruchin.medihelper.presentation.utils

import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.data.utils.AppFirebase

class PicturesStorageRef(
    private val appFirebase: AppFirebase
) {

    fun getPictureRef(pictureName: String): StorageReference {
        val picturesStorage = appFirebase.getMedicinesPicturesStorage()
        return picturesStorage.child(pictureName)
    }
}