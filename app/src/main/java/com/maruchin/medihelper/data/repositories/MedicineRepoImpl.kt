package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.SharedPref
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.framework.getDocumentLive
import com.maruchin.medihelper.data.framework.getDocumentsLive
import com.maruchin.medihelper.data.model.MedicineDb
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MedicineRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val sharedPref: SharedPref
) : MedicineRepo {

    private val medicinesCollectionRef: CollectionReference
        get() = db.collection("users").document(auth.getCurrUserId()).collection("medicines")

    override suspend fun addNew(entity: Medicine) {
        val medicineDb = MedicineDb(entity)
        medicinesCollectionRef.add(medicineDb)
    }

    override suspend fun update(entity: Medicine) {
        val medicineDb = MedicineDb(entity)
        medicinesCollectionRef.document(entity.medicineId).set(medicineDb)
    }

    override suspend fun deleteById(id: String) = withContext(Dispatchers.IO) {
        medicinesCollectionRef.document(id).delete()
        //todo brak usuwania powiązanych rekordów
        return@withContext
    }

    override suspend fun getById(id: String): Medicine? = withContext(Dispatchers.IO) {
        val document = medicinesCollectionRef.document(id).get().await()
        val medicineDb = document.toObject(MedicineDb::class.java)
        val medicine = medicineDb?.toDomainEntity(document.id)
        return@withContext medicine
    }

    override suspend fun getLiveById(id: String): LiveData<Medicine?> = withContext(Dispatchers.IO) {
        val documentLive = medicinesCollectionRef.document(id).getDocumentLive()
        return@withContext Transformations.map(documentLive) {
            val medicineDb = it.toObject(MedicineDb::class.java)
            val medicine = medicineDb?.toDomainEntity(it.id)
            return@map medicine
        }
    }

    override suspend fun getAllList(): List<Medicine> = withContext(Dispatchers.IO) {
        val documentsQuery = medicinesCollectionRef.get().await()
        val medicinesList = mutableListOf<Medicine>()
        documentsQuery.forEach {
            val medicineDb = it.toObject(MedicineDb::class.java)
            val medicine = medicineDb.toDomainEntity(it.id)
            medicinesList.add(medicine)
        }
        return@withContext medicinesList
    }

    override suspend fun getAllListLive(): LiveData<List<Medicine>> = withContext(Dispatchers.IO) {
        val documentsLive = medicinesCollectionRef.getDocumentsLive()
        return@withContext Transformations.map(documentsLive) { snapshotList ->
            val medicinesList = mutableListOf<Medicine>()
            snapshotList.forEach {
                val medicineDb = it.toObject(MedicineDb::class.java)
                val medicine = medicineDb?.toDomainEntity(it.id)
                if (medicine != null) {
                    medicinesList.add(medicine)
                }
            }
            return@map medicinesList.toList()
        }
    }

    override suspend fun getMedicineUnits(): List<String> = withContext(Dispatchers.IO) {
        sharedPref.getMedicineUnitList()
    }
}