package com.maruchin.medihelper.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.utils.DataSharedPref
import com.maruchin.medihelper.data.framework.FirestoreRepo
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.mappers.ProfileMapper
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dataSharedPref: DataSharedPref,
    private val mapper: ProfileMapper
) : FirestoreRepo<Profile>(
    collectionRef = db.collection("users").document(auth.getCurrUserId()).collection("profiles"),
    mapper = mapper
), ProfileRepo {

    private val collectionRef: CollectionReference
        get() = db.collection("users").document(auth.getCurrUserId()).collection("profiles")

    override suspend fun getMainId(): String? = withContext(Dispatchers.IO) {
        val docs = collectionRef.whereEqualTo("mainPerson", true).get().await().documents
        return@withContext if (docs.isNotEmpty()) docs[0].id else null
    }

    override suspend fun getListByMedicine(medicineId: String): List<Profile> {
        //todo ogarnąć czy firebase da radę zrobić takie zapytanie
        return emptyList()
    }

    override suspend fun getColorsList(): List<String> = withContext(Dispatchers.IO) {
        return@withContext dataSharedPref.getProfileColorsList()
    }
}