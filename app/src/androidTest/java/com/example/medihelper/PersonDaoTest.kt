package com.example.medihelper

import com.example.medihelper.localdatabase.dao.PersonDao
import com.example.medihelper.localdatabase.entity.PersonEntity
import org.junit.Test
import org.koin.test.inject
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking

class PersonDaoTest : DatabaseTest() {

    private val personDao: PersonDao by inject()

    @Test
    fun personDao_InsertAndReadEntityList() {
        val newEntity = PersonEntity(
            personName = "Wojtek",
            personColorResID = 1
        )
        val expectedResult = newEntity.copy(personID = 1)

        runBlocking {
            personDao.insert(newEntity)
            val entityList = personDao.getEntityList()
            assertThat(entityList.size).isEqualTo(1)

            val insertedEntity = entityList[0]
            assertThat(insertedEntity).isEqualTo(expectedResult)
        }
    }

    @Test
    fun personDao_InsertUpdateAndReadEntity() {
        val newEntity = PersonEntity(
            personName = "Wojtek",
            personColorResID = 1
        )
        val expectedResult = newEntity.copy(
            personID = 1,
            personColorResID = 3
        )

        runBlocking {
            personDao.insert(newEntity)
            val existingEntity = personDao.getEntity(1)

            val updatedEntity = existingEntity.copy(personColorResID = 3)
            personDao.update(updatedEntity)

            val entityAfterUpdate = personDao.getEntity(1)
            assertThat(entityAfterUpdate).isEqualTo(expectedResult)
        }
    }

    @Test
    fun personDao_InsertListAndDeleteByRemoteIdNotIn() {
        val newEntityList = listOf(
            PersonEntity(
                personRemoteID = 1,
                personName = "Wojtek",
                personColorResID = 5
            ),
            PersonEntity(
                personRemoteID = 2,
                personName = "Paweł",
                personColorResID = 11
            ),
            PersonEntity(
                personRemoteID = 3,
                personName = "Michał",
                personColorResID = 2
            ),
            PersonEntity(
                personRemoteID = 4,
                personName = "Adrian",
                personColorResID = 21
            )
        )
        val expectedResult = arrayOf(
            newEntityList[0].copy(personID = 1),
            newEntityList[2].copy(personID = 3)
        )

        runBlocking {
            personDao.insert(newEntityList)
            var entityList = personDao.getEntityList()
            assertThat(entityList.size).isEqualTo(4)

            val remoteIdList = listOf<Long>(1, 3)
            personDao.deleteByRemoteIdNotIn(remoteIdList)
            entityList = personDao.getEntityList()
            assertThat(entityList).containsExactly(*expectedResult)
        }
    }
}