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

    private val currUserId: String
        get() = auth.getCurrUserId()

    private val currUserDb: DocumentReference
        get() = db.collection(usersCollectionName).document(currUserId)

    private val usersCollectionName = "users"
    private val medicinesCollectionName = "medicines"
    private val profilesCollectionName = "profiles"
    private val plansCollectionName = "plans"
    private val medicinesPicturesFolderName = "medicinesPictures"
    private val typesCollectionName = "types"
    private val medicineUnitsDocumentName = "medicineUnits"
    private val medicineTypesDocumentName = "medicineTypes"

    fun getUsersCollection() = db.collection(usersCollectionName)

    fun getMedicinesCollection() = currUserDb.collection(medicinesCollectionName)

    fun getProfilesCollection() = currUserDb.collection(profilesCollectionName)

    fun getPlansCollection() = currUserDb.collection(plansCollectionName)

    fun getMedicinesPicturesStorage() = storage.reference.child(currUserId).child(medicinesPicturesFolderName)

    fun getMedicineUnitsDocument() = currUserDb.collection(typesCollectionName).document(medicineUnitsDocumentName)

    fun getMedicineTypesDocument() = currUserDb.collection(typesCollectionName).document(medicineTypesDocumentName)

    fun runBatch(batchFunction: (WriteBatch) -> Unit) {
        db.runBatch(batchFunction)
    }
}