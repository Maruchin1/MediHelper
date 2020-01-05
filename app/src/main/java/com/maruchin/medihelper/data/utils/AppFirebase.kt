package com.maruchin.medihelper.data.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.data.framework.getCurrUserId

class AppFirebase(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    val users: CollectionReference
        get() = db.collection(usersCollectionName)

    val medicines: CollectionReference
        get() = currUserDb.collection(medicinesCollectionName)

    val profiles: CollectionReference
        get() = currUserDb.collection(profilesCollectionName)

    val plans: CollectionReference
        get() = currUserDb.collection(plansCollectionName)

    val plannedMedicines: CollectionReference
        get() = currUserDb.collection(plannedMedicinesCollectionName)

    val medicinesPictures: StorageReference
        get() = storage.reference.child(currUserId).child(medicinesPicturesFolderName)

    private val currUserId: String
        get() = auth.getCurrUserId()

    private val currUserDb: DocumentReference
        get() = db.collection(usersCollectionName).document(currUserId)

    private val usersCollectionName = "users"
    private val medicinesCollectionName = "medicines"
    private val profilesCollectionName = "profiles"
    private val plansCollectionName = "plans"
    private val plannedMedicinesCollectionName = "plannedMedicines"
    private val medicinesPicturesFolderName = "medicinesPictures"

    fun runBatch(batchFunction: (WriteBatch) -> Unit) {
        db.runBatch(batchFunction)
    }
}