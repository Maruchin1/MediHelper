package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.model.PlannedMedicineDb
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

    override suspend fun update(entity: PlannedMedicine) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteById(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getById(id: String): PlannedMedicine? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLiveById(id: String): LiveData<PlannedMedicine?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllList(): List<PlannedMedicine> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllListLive(): LiveData<List<PlannedMedicine>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getListByMedicinePlan(medicinePlanId: String): List<PlannedMedicine> =
        withContext(Dispatchers.IO) {
            val docsQuery = collectionRef.whereEqualTo("medicinePlanId", medicinePlanId).get().await()
            val plannedMedicinesList = mutableListOf<PlannedMedicine>()
            docsQuery.forEach {
                val plannedMedicineDb = it.toObject(PlannedMedicineDb::class.java)
                val plannedMedicine = plannedMedicineDb?.toEntity(it.id)
                plannedMedicinesList.add(plannedMedicine)
            }
            return@withContext plannedMedicinesList
        }
}