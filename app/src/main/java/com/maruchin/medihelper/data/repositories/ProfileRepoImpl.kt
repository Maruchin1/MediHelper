package com.maruchin.medihelper.data.repositories

import com.google.firebase.firestore.CollectionReference
import com.maruchin.medihelper.data.utils.DataSharedPref
import com.maruchin.medihelper.data.framework.FirestoreEntityRepo
import com.maruchin.medihelper.data.mappers.ProfileMapper
import com.maruchin.medihelper.data.utils.AppFirebase
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileRepoImpl(
    private val appFirebase: AppFirebase,
    private val dataSharedPref: DataSharedPref,
    private val mapper: ProfileMapper
) : FirestoreEntityRepo<Profile>(
    collectionRef = appFirebase.profiles,
    entityMapper = mapper
), ProfileRepo {

    private val collectionRef: CollectionReference
        get() = appFirebase.profiles

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