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
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
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

    override suspend fun deleteListById(ids: List<String>) = withContext(Dispatchers.IO) {
        db.runBatch { batch ->
            ids.forEach { id ->
                val docRef = collectionRef.document(id)
                batch.delete(docRef)
            }
        }
        return@withContext
    }

    override suspend fun getListByMedicine(medicineId: String): List<MedicinePlan> = withContext(Dispatchers.IO) {
        val docsQuery = collectionRef.whereEqualTo("medicineId", medicineId).get().await()
        return@withContext getEntitiesFromQuery(docsQuery)
    }

    override suspend fun getListByProfile(profileId: String): List<MedicinePlan> = withContext(Dispatchers.IO) {
        val docsQuery = collectionRef.whereEqualTo("profileId", profileId).get().await()
        return@withContext getEntitiesFromQuery(docsQuery)
    }

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