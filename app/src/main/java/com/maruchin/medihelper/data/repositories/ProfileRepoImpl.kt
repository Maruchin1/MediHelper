package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.ProfileColor
import com.maruchin.medihelper.data.SharedPref
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.framework.getDocumentLive
import com.maruchin.medihelper.data.framework.getDocumentsLive
import com.maruchin.medihelper.data.model.ProfileDb
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val sharedPref: SharedPref
) : ProfileRepo {

    private val profilesCollRef: CollectionReference
        get() = db.collection("users").document(auth.getCurrUserId()).collection("profiles")

    init {
        checkDefaultProfileColors()
    }

    override suspend fun addNew(entity: Profile) = withContext(Dispatchers.IO) {
        val profileDb = ProfileDb(entity)
        profilesCollRef.add(profileDb)
        return@withContext
    }

    override suspend fun update(entity: Profile) = withContext(Dispatchers.IO) {
        val profileDb = ProfileDb(entity)
        profilesCollRef.document(entity.profileId).set(profileDb)
        return@withContext
    }

    override suspend fun deleteById(id: String) = withContext(Dispatchers.IO) {
        profilesCollRef.document(id).delete()
        return@withContext
    }

    override suspend fun getById(id: String): Profile? = withContext(Dispatchers.IO) {
        val doc = profilesCollRef.document(id).get().await()
        val profileDb = doc.toObject(ProfileDb::class.java)
        val profile = profileDb?.toProfile(doc.id)
        return@withContext profile
    }

    override suspend fun getLiveById(id: String): LiveData<Profile?> = withContext(Dispatchers.IO) {
        val docLive = profilesCollRef.document(id).getDocumentLive()
        return@withContext Transformations.map(docLive) {
            val profileDb = it.toObject(ProfileDb::class.java)
            val profile = profileDb?.toProfile(it.id)
            return@map profile
        }
    }

    override suspend fun getAllList(): List<Profile> = withContext(Dispatchers.IO) {
        val docsQuery = profilesCollRef.get().await()
        val profilesList = mutableListOf<Profile>()
        docsQuery.forEach {
            val profileDb = it.toObject(ProfileDb::class.java)
            val profile = profileDb.toProfile(it.id)
            profilesList.add(profile)
        }
        return@withContext profilesList
    }

    override suspend fun getAllListLive(): LiveData<List<Profile>> = withContext(Dispatchers.IO) {
        val docsLive = profilesCollRef.getDocumentsLive()
        return@withContext Transformations.map(docsLive) { snapshotList ->
            val profilesList = mutableListOf<Profile>()
            snapshotList.forEach {
                val profileDb = it.toObject(ProfileDb::class.java)
                val profile = profileDb?.toProfile(it.id)
                if (profile != null) {
                    profilesList.add(profile)
                }
            }
            return@map profilesList.toList()
        }
    }

    override suspend fun getMainId(): String? = withContext(Dispatchers.IO) {
        val docs = profilesCollRef.whereEqualTo("mainPerson", true).get().await().documents
        return@withContext if (docs.isNotEmpty())  docs[0].id else null
    }

    override suspend fun getListByMedicine(medicineId: String): List<Profile> {
        //todo ogarnąć czy firebase da radę zrobić takie zapytanie
        return emptyList()
    }

    private fun checkDefaultProfileColors() {
        val profileColors = sharedPref.getProfileColorsList()
        if (profileColors.isNullOrEmpty()) {
            sharedPref.saveProfileColorsList(getDefaultProfileColors())
        }
    }

    private fun getDefaultProfileColors() = listOf(
        ProfileColor.MAIN,
        ProfileColor.PURPLE,
        ProfileColor.BLUE,
        ProfileColor.BROWN,
        ProfileColor.CYAN,
        ProfileColor.GRAY,
        ProfileColor.LIGHT_GREEN,
        ProfileColor.ORANGE,
        ProfileColor.YELLOW
    ).map { it.colorString }
}