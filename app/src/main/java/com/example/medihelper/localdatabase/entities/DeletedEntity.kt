package com.example.medihelper.localdatabase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "deleted_entities", primaryKeys = ["entity_type", "entity_remote_id"])
data class DeletedEntity(

    @ColumnInfo(name = "entity_type")
    val entityType: EntityType,

    @ColumnInfo(name = "entity_remote_id")
    val entityRemoteID: Long
) {
    enum class EntityType {
        MEDICINE, PERSON, MEDICINE_PLAN, PLANNED_MEDICINE
    }
}