package com.maruchin.medihelper.data.framework

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.google.firebase.firestore.CollectionReference
import com.maruchin.medihelper.domain.framework.BaseEntity
import com.maruchin.medihelper.domain.framework.BaseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

open class FirestoreRepo<T : BaseEntity>(
    private val collectionRef: CollectionReference,
    private val mapper: BaseMapper<T>
) : BaseRepo<T> {

    override suspend fun addNew(entity: T): String? = withContext(Dispatchers.IO) {
        val data = mapper.entityToMap(entity)
        val newDoc = collectionRef.document()
        newDoc.set(data)
        return@withContext newDoc.id
    }

    override suspend fun update(entity: T) = withContext(Dispatchers.IO) {
        val data = mapper.entityToMap(entity)
        collectionRef.document(entity.entityId).set(data)
        return@withContext
    }

    override suspend fun deleteById(entityId: String) = withContext(Dispatchers.IO) {
        collectionRef.document(entityId).delete()
        return@withContext
    }

    override suspend fun getById(entityId: String): T? = withContext(Dispatchers.IO) {
        val doc = collectionRef.document(entityId).get().await()
        val entity = doc.data?.let { data ->
            mapper.mapToEntity(entityId, data)
        }
        return@withContext entity
    }

    override suspend fun getLiveById(entityId: String): LiveData<T> = withContext(Dispatchers.IO) {
        val docLive = collectionRef.document(entityId).getDocumentLive()
        return@withContext Transformations.switchMap(docLive) {
            liveData {
                val value = mapper.mapToEntity(it.id, it.data!!)
                emit(value)
            }
        }
    }

    override suspend fun getAllList(): List<T> = withContext(Dispatchers.IO) {
        val docsQuery = collectionRef.get().await()
        return@withContext docsQuery.map {
            mapper.mapToEntity(it.id, it.data)
        }
    }

    override suspend fun getAllListLive(): LiveData<List<T>> = withContext(Dispatchers.IO) {
        val docListLive = collectionRef.getDocumentsLive()
        return@withContext Transformations.switchMap(docListLive) { snapshotList ->
            liveData {
                val value = snapshotList.map {
                    mapper.mapToEntity(it.id, it.data!!)
                }
                emit(value)
            }
        }
    }

}