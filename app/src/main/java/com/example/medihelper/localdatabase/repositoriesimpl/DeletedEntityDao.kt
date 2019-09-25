package com.example.medihelper.localdatabase.repositoriesimpl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medihelper.localdatabase.entities.DeletedEntity


@Dao
interface DeletedEntityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(deletedEntity: DeletedEntity)

    @Query("SELECT entity_remote_id FROM deleted_entities WHERE entity_type = :entityType")
    suspend fun getDeletedRemoteIDList(entityType: DeletedEntity.EntityType): List<Long>

    @Query("DELETE FROM deleted_entities WHERE entity_type = :entityType")
    suspend fun deleteDeleterRemoteIDList(entityType: DeletedEntity.EntityType)
}