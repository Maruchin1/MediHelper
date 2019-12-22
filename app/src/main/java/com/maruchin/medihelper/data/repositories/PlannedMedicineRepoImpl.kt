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
import com.maruchin.medihelper.data.mappers.PlannedMedicineMapper
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PlannedMedicineRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val mapper: PlannedMedicineMapper
) : FirestoreRepo<PlannedMedicine>(
    collectionRef = db.collection("users").document(auth.getCurrUserId())
        .collection("plannedMedicines"),
    mapper = mapper
), PlannedMedicineRepo {

    private val collectionRef: CollectionReference
        get() = db.collection("users").document(auth.getCurrUserId()).collection("plannedMedicines")

    override suspend fun addNewList(entityList: List<PlannedMedicine>) = withContext(Dispatchers.IO) {
        db.runBatch { batch ->
            entityList.forEach { entity ->
                val newDoc = collectionRef.document()
                val data = runBlocking {
                    mapper.entityToMap(entity)
                }
                batch.set(newDoc, data)
            }
        }
        return@withContext
    }

    override suspend fun deleteListById(entityIdList: List<String>) = withContext(Dispatchers.IO) {
        db.runBatch { batch ->
            entityIdList.forEach { entityId ->
                val doc = collectionRef.document(entityId)
                batch.delete(doc)
            }
        }
        return@withContext
    }

    override suspend fun getListByMedicinePlan(medicinePlanId: String): List<PlannedMedicine> =
        withContext(Dispatchers.IO) {
            val docsQuery = collectionRef.whereEqualTo("medicinePlanId", medicinePlanId).get().await()
            return@withContext withContext(Dispatchers.Default) {
                docsQuery.map {
                    mapper.mapToEntity(it.id, it.data)
                }
            }
        }

    override suspend fun getLiveListByProfileAndDate(
        profileId: String,
        date: AppDate
    ): LiveData<List<PlannedMedicine>> = withContext(Dispatchers.IO) {
        val docsLive = collectionRef.whereEqualTo("profileId", profileId)
            .whereEqualTo("plannedDate", date.jsonFormatString).getDocumenstLive()
        return@withContext Transformations.switchMap(docsLive) { snapshotList ->
            liveData {
                val value = withContext(Dispatchers.Default) {
                    snapshotList.map {
                        mapper.mapToEntity(it.id, it.data!!)
                    }
                }
                emit(value)
            }
        }
    }
}