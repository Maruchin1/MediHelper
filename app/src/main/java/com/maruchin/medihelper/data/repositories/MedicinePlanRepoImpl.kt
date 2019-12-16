package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.model.MedicinePlanContinuousDb
import com.maruchin.medihelper.data.model.MedicinePlanOnceDb
import com.maruchin.medihelper.data.model.MedicinePlanPeriodDb
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.MedicinePlanContinuous
import com.maruchin.medihelper.domain.entities.MedicinePlanOnce
import com.maruchin.medihelper.domain.entities.MedicinePlanPeriod
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
        when (entity) {
            is MedicinePlanOnce -> {
                val medicinePlanDb = MedicinePlanOnceDb(entity)
                collectionRef.add(medicinePlanDb)
            }
            is MedicinePlanPeriod -> {
                val medicinePlanDb = MedicinePlanPeriodDb(entity)
                collectionRef.add(medicinePlanDb)
            }
            is MedicinePlanContinuous -> {
                val medicinePlanDb = MedicinePlanContinuousDb(entity)
                collectionRef.add(medicinePlanDb)
            }
        }
        return@withContext
    }

    override suspend fun update(entity: MedicinePlan) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteById(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getById(id: String): MedicinePlan? {
        val doc = collectionRef.document(id).get().await()
        val planType = doc.data?.get("planType")

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLiveById(id: String): LiveData<MedicinePlan?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllList(): List<MedicinePlan> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllListLive(): LiveData<List<MedicinePlan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}