package com.maruchin.medihelper.data.repositories

//import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo

//class PlannedMedicineRepoImpl(
//    private val appFirebase: AppFirebase,
//    private val mapper: PlannedMedicineMapper
//) : FirestoreEntityRepo<PlannedMedicine>(
//    collectionRef = appFirebase.plannedMedicines,
//    entityMapper = mapper
//), PlannedMedicineRepo {
//
//    private val collectionRef: CollectionReference
//        get() = appFirebase.plannedMedicines
//
//    override suspend fun addNewList(entityList: List<PlannedMedicine>): List<PlannedMedicine> = withContext(Dispatchers.IO) {
//        val added = mutableListOf<PlannedMedicine>()
//        appFirebase.runBatch { batch ->
//            entityList.forEach { entity ->
//                val newDoc = collectionRef.document()
//                val data = runBlocking {
//                    mapper.entityToMap(entity)
//                }
//
//                added.add(entity.copy(entityId = newDoc.id))
//                batch.set(newDoc, data)
//            }
//        }
//        return@withContext added
//    }
//
//    override suspend fun deleteListById(entityIdList: List<String>) = withContext(Dispatchers.IO) {
//        appFirebase.runBatch { batch ->
//            entityIdList.forEach { entityId ->
//                val doc = collectionRef.document(entityId)
//                batch.delete(doc)
//            }
//        }
//        return@withContext
//    }
//
//    override suspend fun getListByMedicinePlan(medicinePlanId: String): List<PlannedMedicine> =
//        withContext(Dispatchers.IO) {
//            val docsQuery = collectionRef.whereEqualTo(mapper.medicinePlanId, medicinePlanId).get().await()
//            return@withContext docsQuery.map {
//                mapper.mapToEntity(it.id, it.data)
//            }
//        }
//
//    override suspend fun getListNotTakenForLastMinutes(minutes: Int): List<PlannedMedicine> {
//        val currTimeInMillis = getCurrTimeInMillis()
//        val minTimeInMillis = calculateMinTimeInMillis(currTimeInMillis, minutes)
//        val docsQuery = collectionRef
//            .whereGreaterThanOrEqualTo(mapper.plannedDateTimeMillis, minTimeInMillis)
//            .whereLessThanOrEqualTo(mapper.plannedDateTimeMillis, currTimeInMillis)
//            .whereEqualTo(mapper.status, PlannedMedicine.Status.NOT_TAKEN.toString())
//            .get().await()
//        return super.getEntitiesFromQuery(docsQuery)
//    }
//
//    override suspend fun getLiveListByProfileAndDate(
//        profileId: String,
//        date: AppDate
//    ): LiveData<List<PlannedMedicine>> = withContext(Dispatchers.IO) {
//        val docsLive = collectionRef.whereEqualTo(mapper.profileId, profileId)
//            .whereEqualTo(mapper.plannedDate, date.jsonFormatString).getDocumenstLive()
//        return@withContext Transformations.switchMap(docsLive) { snapshotList ->
//            liveData {
//                val value = snapshotList.map {
//                    mapper.mapToEntity(it.id, it.data!!)
//                }
//                emit(value)
//            }
//        }
//    }
//
//    private fun getCurrTimeInMillis(): Long {
//        val currCalendar = Calendar.getInstance()
//        return currCalendar.timeInMillis
//    }
//
//    private fun calculateMinTimeInMillis(currTimeMillis: Long, minutes: Int): Long {
//        val millisRange = minutesToMillis(minutes)
//        return currTimeMillis - millisRange
//    }
//
//    private fun minutesToMillis(minutes: Int): Long {
//        return minutes * 60 * 1000L
//    }
//}