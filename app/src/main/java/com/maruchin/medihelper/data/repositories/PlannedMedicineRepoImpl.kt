package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.framework.getDocumenstLive
import com.maruchin.medihelper.data.model.PlannedMedicineDb
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PlannedMedicineRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : PlannedMedicineRepo {

    private val collectionRef: CollectionReference
        get() = db.collection("users").document(auth.getCurrUserId()).collection("plannedMedicines")

    override suspend fun addNew(entity: PlannedMedicine): String? = withContext(Dispatchers.IO) {
        val medicineCalendarEntryDb = PlannedMedicineDb(entity)
        val newDoc = collectionRef.document()
        newDoc.set(medicineCalendarEntryDb)
        return@withContext newDoc.id
    }

    override suspend fun update(entity: PlannedMedicine) = withContext(Dispatchers.IO) {
        val plannedMedicineDb = PlannedMedicineDb(entity)
        collectionRef.document(entity.plannedMedicineId).set(plannedMedicineDb)
        return@withContext
    }

    override suspend fun deleteById(id: String) = withContext(Dispatchers.IO) {
        collectionRef.document(id).delete()
        return@withContext
    }

    override suspend fun getById(id: String): PlannedMedicine? = withContext(Dispatchers.IO) {
        val doc = collectionRef.document(id).get().await()
        val plannedMedicineDb = doc.toObject(PlannedMedicineDb::class.java)
        val plannedMedicine = plannedMedicineDb?.toEntity(doc.id)
        return@withContext plannedMedicine
    }

    override suspend fun getLiveById(id: String): LiveData<PlannedMedicine?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllList(): List<PlannedMedicine> = withContext(Dispatchers.IO){
        val plannedMedicinesList = mutableListOf<PlannedMedicine>()
        val docsQuery = collectionRef.get().await()
        docsQuery.forEach {
            val plannedMedicineDb = it.toObject(PlannedMedicineDb::class.java)
            val plannedMedicine = plannedMedicineDb.toEntity(it.id)
            plannedMedicinesList.add(plannedMedicine)
        }
        return@withContext plannedMedicinesList
    }

    override suspend fun getAllListLive(): LiveData<List<PlannedMedicine>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getListByMedicinePlan(medicinePlanId: String): List<PlannedMedicine> =
        withContext(Dispatchers.IO) {
            val plannedMedicinesList = mutableListOf<PlannedMedicine>()
            val docsQuery = collectionRef.whereEqualTo("medicinePlanId", medicinePlanId).get().await()
            docsQuery.forEach {
                val plannedMedicineDb = it.toObject(PlannedMedicineDb::class.java)
                val plannedMedicine = plannedMedicineDb.toEntity(it.id)
                plannedMedicinesList.add(plannedMedicine)
            }
            return@withContext plannedMedicinesList
        }

    override suspend fun getLiveListByProfileAndDate(profileId: String, date: AppDate): LiveData<List<PlannedMedicine>> = withContext(Dispatchers.IO) {
        val docsLive = collectionRef.whereEqualTo("profileId", profileId)
            .whereEqualTo("plannedDate", date.jsonFormatString).getDocumenstLive()
        return@withContext Transformations.map(docsLive) { snapshotList ->
            val plannedMedicinesList = mutableListOf<PlannedMedicine>()
            snapshotList.forEach {
                val plannedMedicineDb = it.toObject(PlannedMedicineDb::class.java)
                val plannedMedicine = plannedMedicineDb?.toEntity(it.id)
                if (plannedMedicine != null) {
                    plannedMedicinesList.add(plannedMedicine)
                }
            }
            return@map plannedMedicinesList.toList()
        }
    }
}