package com.maruchin.medihelper.data.framework

interface FirebaseEntity<DomainEntity> {

    fun toDomainEntity(id: String): DomainEntity
}