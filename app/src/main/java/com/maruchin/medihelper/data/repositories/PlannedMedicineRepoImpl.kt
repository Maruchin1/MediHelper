package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.google.firebase.firestore.CollectionReference
import com.maruchin.medihelper.data.framework.FirestoreEntityRepo
import com.maruchin.medihelper.data.framework.getDocumenstLive
import com.maruchin.medihelper.data.mappers.PlannedMedicineMapper
import com.maruchin.medihelper.data.utils.AppFirebase
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class PlannedMedicineRepoImpl(
    private val appFirebase: AppFirebase,
    private val mapper: PlannedMedicineMapper
) : FirestoreEntityRepo<PlannedMedicine>(
    collectionRef = appFirebase.plannedMedicines,
    entityMapper = mapper
), PlannedMedicineRepo {

    private val collectionRef: CollectionReference
        get() = appFirebase.plannedMedicines

    override suspend fun addNewList(entityList: List<PlannedMedicine>): List<PlannedMedicine> =
        withContext(Dispatchers.IO) {
            val added = mutableListOf<PlannedMedicine>()
            appFirebase.runBatch { batch ->
                entityList.forEach { entity ->
                    val newDoc = collectionRef.document()
                    val data = runBlocking {
                        mapper.entityToMap(entity)
                    }

                    added.add(entity.copy(entityId = newDoc.id))
                    batch.set(newDoc, data)
                }
            }
            return@withContext added
        }

    override suspend fun deleteListById(entityIdList: List<String>) = withContext(Dispatchers.IO) {
        appFirebase.runBatch { batch ->
            entityIdList.forEach { entityId ->
                val doc = collectionRef.document(entityId)
                batch.delete(doc)
            }
        }
        return@withContext
    }

    override suspend fun getListByMedicinePlan(medicinePlanId: String): List<PlannedMedicine> =
        withContext(Dispatchers.IO) {
            val docsQuery = collectionRef.whereEqualTo(mapper.medicinePlanId, medicinePlanId).get().await()
            return@withContext docsQuery.map {
                mapper.mapToEntity(it.id, it.data)
            }
        }

    override suspend fun getListByMedicinePlanBeforeDate(medicinePlanId: String, date: AppDate): List<PlannedMedicine> =
        withContext(Dispatchers.IO) {
            val docsQuery = collectionRef
                .whereEqualTo(mapper.medicinePlanId, medicinePlanId)
                .whereLessThanOrEqualTo(mapper.plannedDate, date.jsonFormatString)
                .get().await()
            return@withContext docsQuery.map {
                mapper.mapToEntity(it.id, it.data)
            }
        }

    override suspend fun getListNotTakenForDay(date: AppDate, time: AppTime): List<PlannedMedicine> =
        withContext(Dispatchers.IO) {
            val docsQuery = collectionRef
                .whereEqualTo(mapper.plannedDate, date.jsonFormatString)
                .whereLessThan(mapper.plannedTime, time.jsonFormatString)
                .whereEqualTo(mapper.status, PlannedMedicine.Status.NOT_TAKEN.toString())
                .get().await()
            return@withContext super.getEntitiesFromQuery(docsQuery)
        }

    override suspend fun getListNotTakenForNextMinutes(minutes: Int): List<PlannedMedicine> =
        withContext(Dispatchers.IO) {
            val currTimeMillis = getCurrTimeInMillis()
            val maxTimeInMillis = calculateMaxTimeInMillis(currTimeMillis, minutes)
            val docQuery = collectionRef
                .whereLessThanOrEqualTo(mapper.plannedDateTimeMillis, maxTimeInMillis)
                .whereGreaterThanOrEqualTo(mapper.plannedDateTimeMillis, currTimeMillis)
                .whereEqualTo(mapper.status, PlannedMedicine.Status.NOT_TAKEN.toString())
                .get().await()
            return@withContext super.getEntitiesFromQuery(docQuery)
        }

    override suspend fun getLiveListByProfileAndDate(
        profileId: String,
        date: AppDate
    ): LiveData<List<PlannedMedicine>> = withContext(Dispatchers.IO) {
        val docsLive = collectionRef.whereEqualTo(mapper.profileId, profileId)
            .whereEqualTo(mapper.plannedDate, date.jsonFormatString).getDocumenstLive()
        return@withContext Transformations.switchMap(docsLive) { snapshotList ->
            liveData {
                val value = snapshotList.map {
                    mapper.mapToEntity(it.id, it.data!!)
                }
                emit(value)
            }
        }
    }

    private fun getCurrTimeInMillis(): Long {
        val currCalendar = Calendar.getInstance()
        return currCalendar.timeInMillis
    }

    private fun calculateMinTimeInMillis(currTimeMillis: Long, minutes: Int): Long {
        val millisRange = minutesToMillis(minutes)
        return currTimeMillis - millisRange
    }

    private fun calculateMaxTimeInMillis(currTimeMillis: Long, minutes: Int): Long {
        val millisRange = minutesToMillis(minutes)
        return currTimeMillis + millisRange
    }

    private fun minutesToMillis(minutes: Int): Long {
        return minutes * 60 * 1000L
    }
}