package com.maruchin.medihelper.data.framework

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.google.firebase.firestore.DocumentReference
import com.maruchin.medihelper.data.mappers.TypeContainer
import com.maruchin.medihelper.data.mappers.TypeContainerMapper
import com.maruchin.medihelper.domain.framework.BaseTypeRepo
import kotlinx.coroutines.tasks.await

abstract class FirestoreTypeRepo(
    private val documentRef: DocumentReference,
    private val mapper: TypeContainerMapper
) : BaseTypeRepo {

    override suspend fun init(types: List<String>) {
        val newContainer = TypeContainer(types)
        setContainer(newContainer)
    }

    override suspend fun addNewDistinct(type: String) {
        val container = getContainer()
        val typesCollection = container.types
        if (!typesCollection.contains(type)) {
            val updatedUnits = typesCollection.toMutableList()
            updatedUnits.add(type)
            container.types = updatedUnits
            setContainer(container)
        }
    }

    override suspend fun delete(type: String) {
        val container = getContainer()
        val typesCollection = container.types.toMutableList()
        typesCollection.remove(type)
        container.types = typesCollection
        setContainer(container)
    }

    override suspend fun getAll(): List<String> {
        val container = getContainer()
        return container.types
    }

    override suspend fun getLiveAll(): LiveData<List<String>> {
        val containerLive = getLiveContainer()
        return Transformations.map(containerLive) {
            it.types
        }
    }

    private suspend fun getContainer(): TypeContainer {
        val doc = documentRef.get().await()
        val map = doc.data!!
        return mapper.mapToContainer(map)
    }

    private suspend fun getLiveContainer(): LiveData<TypeContainer> {
        val docLive = documentRef.getDocumentLive()
        return Transformations.switchMap(docLive) {
            val map = it.data!!
            liveData {
                val container = mapper.mapToContainer(map)
                emit(container)
            }
        }
    }

    private suspend fun setContainer(newContainer: TypeContainer) {
        val map = mapper.containerToMap(newContainer)
        documentRef.set(map)
    }
}