package com.maruchin.medihelper.data.framework

import com.google.firebase.firestore.DocumentReference
import com.maruchin.medihelper.data.mappers.TypeContainer
import com.maruchin.medihelper.data.mappers.TypeContainerMapper
import com.maruchin.medihelper.domain.framework.BaseTypeRepo
import kotlinx.coroutines.tasks.await

abstract class FirestoreTypeRepo(
    private val documentRef: DocumentReference,
    private val mapper: TypeContainerMapper
) : BaseTypeRepo {

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

    private suspend fun getContainer(): TypeContainer {
        val doc = documentRef.get().await()
        val map = doc.data!!
        return mapper.mapToContainer(map)
    }

    private suspend fun setContainer(newContainer: TypeContainer) {
        val map = mapper.containerToMap(newContainer)
        documentRef.set(map)
    }
}