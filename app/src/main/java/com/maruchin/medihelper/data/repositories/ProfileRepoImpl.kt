package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.model.ProfileDb
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.repositories.ProfileRepo

class ProfileRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ProfileRepo {

    override suspend fun addNew(entity: Profile) {
        val userId = auth.getCurrUserId()
        val profileDb = ProfileDb(
            name = entity.name,
            color = entity.color,
            mainPerson = entity.mainPerson
        )
        db.collection("users").document(userId)
            .collection("profiles").add(profileDb)
    }

    override suspend fun update(entity: Profile) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteById(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getById(id: String): Profile? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLiveById(id: String): LiveData<Profile?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllList(): List<Profile> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllListLive(): LiveData<List<Profile>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getListByMedicine(medicineId: String): List<Profile> {
        //todo ogarnąć czy firebase da radę zrobić takie zapytanie
        return emptyList()
    }
}