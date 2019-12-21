package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.FirestoreRepo
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.framework.getDocumenstLive
import com.maruchin.medihelper.data.mappers.MedicinePlanMapper
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MedicinePlanRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val mapper: MedicinePlanMapper
) : FirestoreRepo<MedicinePlan>(
    collectionRef = db.collection("users").document(auth.getCurrUserId())
        .collection("medicinesPlans"),
    mapper = mapper
), MedicinePlanRepo {

    private val collectionRef: CollectionReference
        get() = db.collection("users").document(auth.getCurrUserId()).collection("medicinesPlans")

    override suspend fun getListLiveByProfile(profileId: String): LiveData<List<MedicinePlan>> =
        withContext(Dispatchers.IO) {
            val docsLive = collectionRef.whereEqualTo("profileId", profileId).getDocumenstLive()
            return@withContext Transformations.switchMap(docsLive) { snapshotList ->
                liveData {
                    val value = snapshotList.map {
                        mapper.mapToEntity(it.id, it.data!!)
                    }
                    emit(value)
                }
            }
        }
}