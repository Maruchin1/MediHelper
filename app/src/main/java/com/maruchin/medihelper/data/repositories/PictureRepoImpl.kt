package com.maruchin.medihelper.data.repositories

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.data.utils.AppFirebase
import com.maruchin.medihelper.domain.repositories.PictureRepo
import kotlinx.coroutines.tasks.await
import java.io.File

class PictureRepoImpl(
    private val appFirebase: AppFirebase
) : PictureRepo {

    private val storage: StorageReference by lazy {
        appFirebase.getMedicinesPicturesStorage()
    }

    override suspend fun saveMedicinePicture(pictureFile: File) {
        val pictureFileRef = storage.child(pictureFile.name)
        val fileUri = Uri.fromFile(pictureFile)
        pictureFileRef.putFile(fileUri).await()
    }

    override suspend fun deleteMedicinePicture(pictureName: String) {
        val pictureFileRef = storage.child(pictureName)
        pictureFileRef.delete().await()
    }
}