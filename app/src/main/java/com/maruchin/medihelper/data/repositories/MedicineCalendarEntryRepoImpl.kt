package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.model.MedicineCalendarEntryDb
import com.maruchin.medihelper.domain.entities.MedicineCalendarEntry
import com.maruchin.medihelper.domain.repositories.MedicineCalendarEntryRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MedicineCalendarEntryRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MedicineCalendarEntryRepo {

    override suspend fun addNew(entity: MedicineCalendarEntry) = withContext(Dispatchers.IO) {
        val medicineCalendarEntryDb = MedicineCalendarEntryDb(entity)
        getCollectionRef(entity.medicinePlanId).add(medicineCalendarEntryDb)
        return@withContext
    }

    override suspend fun update(entity: MedicineCalendarEntry) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteById(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getById(id: String): MedicineCalendarEntry? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLiveById(id: String): LiveData<MedicineCalendarEntry?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllList(): List<MedicineCalendarEntry> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllListLive(): LiveData<List<MedicineCalendarEntry>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getCollectionRef(medicinePlanId: String): CollectionReference {
        return db.collection("users").document(auth.getCurrUserId())
            .collection("medicinesPlans").document(medicinePlanId)
            .collection("medicinesCalendarEntries")
    }
}