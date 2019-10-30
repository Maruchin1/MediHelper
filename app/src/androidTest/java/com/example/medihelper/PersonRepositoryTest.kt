package com.example.medihelper

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medihelper.localdatabase.AppDatabase
import com.example.medihelper.localdatabase.entity.PersonEntity
import com.example.medihelper.localdatabase.repositories.PersonRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import com.google.common.truth.Truth.assertThat
import org.koin.core.context.loadKoinModules

@RunWith(AndroidJUnit4::class)
class PersonRepositoryTest : KoinTest {

    private val personRepository: PersonRepository by inject()

    @Before
    fun before() {
        loadKoinModules(listOf(testDatabaseModule, repositoryModule))
    }

    @Test
    fun insertPersonAndReadList() {
        val personEntity = PersonEntity(
            personName = "Wojtek",
            personColorResID = 1
        )

        runBlocking {
            personRepository.insert(personEntity)
            val personList = personRepository.getEntityList()
            assertThat(personList).containsExactly(
                PersonEntity(
                    personID = 1,
                    personName = "Wojtek",
                    personColorResID = 1
                )
            )
        }
    }

    @Test
    fun insertPersonUpdateNameAndReadList() {
        val personEntity = PersonEntity(
            personName = "Wojtek",
            personColorResID = 1
        )
        val updatedPersonEntity = PersonEntity(
            personID = 1,
            personName = "Paweł",
            personColorResID = 1
        )

        runBlocking {
            personRepository.insert(personEntity)
            personRepository.update(updatedPersonEntity)
            val personList = personRepository.getEntityList()
            assertThat(personList).containsExactly(
                PersonEntity(
                    personID = 1,
                    personName = "Paweł",
                    personColorResID = 1
                )
            )
        }
    }
}