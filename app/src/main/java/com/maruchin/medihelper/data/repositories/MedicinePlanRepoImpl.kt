package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.framework.getDocumenstLive
import com.maruchin.medihelper.data.framework.getDocumentsLive
import com.maruchin.medihelper.data.model.MedicinePlanDb
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MedicinePlanRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MedicinePlanRepo {

    private val collectionRef: CollectionReference
        get() = db.collection("users").document(auth.getCurrUserId()).collection("medicinesPlans")

    override suspend fun addNew(entity: MedicinePlan) = withContext(Dispatchers.IO) {
        val medicinePlanDb = MedicinePlanDb(entity)
        collectionRef.add(medicinePlanDb)
        return@withContext
    }

    override suspend fun update(entity: MedicinePlan) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteById(id: String) = withContext(Dispatchers.IO) {
        collectionRef.document(id).delete()
        return@withContext
    }

    override suspend fun getById(id: String): MedicinePlan? = withContext(Dispatchers.IO) {
        val doc = collectionRef.document(id).get().await()
        val medicinePlanDb = doc.toObject(MedicinePlanDb::class.java)
        val medicinePlan = medicinePlanDb?.toEntity(doc.id)
        return@withContext medicinePlan
    }

    override suspend fun getLiveById(id: String): LiveData<MedicinePlan?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllList(): List<MedicinePlan> = withContext(Dispatchers.IO) {
        val docQuery = collectionRef.get().await()
        val medicinesPlansList = mutableListOf<MedicinePlan>()
        docQuery.forEach {
            val medicinePlanDb = it.toObject(MedicinePlanDb::class.java)
            val medicinePlan = medicinePlanDb.toEntity(it.id)
            medicinesPlansList.add(medicinePlan)
        }
        return@withContext medicinesPlansList
    }

    override suspend fun getAllListLive(): LiveData<List<MedicinePlan>> = withContext(Dispatchers.IO) {
        val docsLive = collectionRef.getDocumentsLive()
        return@withContext Transformations.map(docsLive) { snapshotList ->
            val medicinesPlansList = mutableListOf<MedicinePlan>()
            snapshotList.forEach {
                val medicinePlanDb = it.toObject(MedicinePlanDb::class.java)
                val medicinePlan = medicinePlanDb?.toEntity(it.id)
                if (medicinePlan != null) {
                    medicinesPlansList.add(medicinePlan)
                }
            }
            return@map medicinesPlansList.toList()
        }
    }

    override suspend fun getListLiveByProfile(profileId: String): LiveData<List<MedicinePlan>> =
        withContext(Dispatchers.IO) {
            val docsLive = collectionRef.whereEqualTo("profileId", profileId).getDocumenstLive()
            return@withContext Transformations.map(docsLive) { snapshotList ->
                val medicinesPlansList = mutableListOf<MedicinePlan>()
                snapshotList.forEach {
                    val medicinePlanDb = it.toObject(MedicinePlanDb::class.java)
                    val medicinePlan = medicinePlanDb?.toEntity(it.id)
                    if (medicinePlan != null) {
                        medicinesPlansList.add(medicinePlan)
                    }
                }
                return@map medicinesPlansList.toList()
            }
        }
}