package com.maruchin.medihelper.data.repositories

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
import java.io.File

class MedicineRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val sharedPref: SharedPref
) : MedicineRepo {

    private val medicinesCollRef: CollectionReference
        get() = db.collection("users").document(auth.getCurrUserId()).collection("medicines")

    init {
        checkDefaultMedicineUnits()
    }

    override suspend fun addNew(entity: Medicine) = withContext(Dispatchers.IO) {
        val medicineDb = MedicineDb(entity)
        medicinesCollRef.add(medicineDb)
        return@withContext
    }

    override suspend fun update(entity: Medicine) = withContext(Dispatchers.IO) {
        val medicineDb = MedicineDb(entity)
        medicinesCollRef.document(entity.medicineId).set(medicineDb)
        return@withContext
    }

    override suspend fun deleteById(id: String) = withContext(Dispatchers.IO) {
        medicinesCollRef.document(id).delete()
        return@withContext
    }

    override suspend fun getById(id: String): Medicine? = withContext(Dispatchers.IO) {
        val doc = medicinesCollRef.document(id).get().await()
        val medicineDb = doc.toObject(MedicineDb::class.java)
        val medicine = medicineDb?.toMedicine(doc.id)
        return@withContext medicine
    }

    override suspend fun getLiveById(id: String): LiveData<Medicine?> = withContext(Dispatchers.IO) {
        val docLive = medicinesCollRef.document(id).getDocumentLive()
        return@withContext Transformations.map(docLive) {
            val medicineDb = it.toObject(MedicineDb::class.java)
            val medicine = medicineDb?.toMedicine(it.id)
            return@map medicine
        }
    }

    override suspend fun getAllList(): List<Medicine> = withContext(Dispatchers.IO) {
        val docsQuery = medicinesCollRef.get().await()
        val medicinesList = mutableListOf<Medicine>()
        docsQuery.forEach {
            val medicineDb = it.toObject(MedicineDb::class.java)
            val medicine = medicineDb.toMedicine(it.id)
            medicinesList.add(medicine)
        }
        return@withContext medicinesList
    }

    override suspend fun getAllListLive(): LiveData<List<Medicine>> = withContext(Dispatchers.IO) {
        val docsLive = medicinesCollRef.getDocumentsLive()
        return@withContext Transformations.map(docsLive) { snapshotList ->
            val medicinesList = mutableListOf<Medicine>()
            snapshotList.forEach {
                val medicineDb = it.toObject(MedicineDb::class.java)
                val medicine = medicineDb?.toMedicine(it.id)
                if (medicine != null) {
                    medicinesList.add(medicine)
                }
            }
            return@map medicinesList.toList()
        }
    }

    override suspend fun saveMedicinePicture(pictureFile: File) {
        val pictureFileRef = storage.reference.child(pictureFile.name)
        val fileUri = Uri.fromFile(pictureFile)
        pictureFileRef.putFile(fileUri).await()
    }

    override suspend fun deleteMedicinePicture(pictureName: String) {
        val pictureFileRef = storage.reference.child(pictureName)
        pictureFileRef.delete().await()
    }

    override suspend fun getMedicineUnits(): List<String> = withContext(Dispatchers.IO) {
        sharedPref.getMedicineUnitList()
    }

    private fun checkDefaultMedicineUnits() {
        val medicineUnits = sharedPref.getMedicineUnitList()
        if (medicineUnits.isNullOrEmpty()) {
            sharedPref.saveMedicineUnitList(getDefaultMedicineUnits())
        }
    }

    private fun getDefaultMedicineUnits() = listOf("dawki", "tabletki", "ml", "g", "mg", "pastylki")
}