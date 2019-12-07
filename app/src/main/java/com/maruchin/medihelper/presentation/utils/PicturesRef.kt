package com.maruchin.medihelper.presentation.utils

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PicturesRef(
    private val firebaseStorage: FirebaseStorage
) {
    fun get(pictureName: String): StorageReference {
        return firebaseStorage.reference.child(pictureName)
    }
}