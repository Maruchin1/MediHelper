package com.example.medihelper.data.repositories

import androidx.test.platform.app.InstrumentationRegistry
import com.example.medihelper.AppAndroidTest
import com.example.medihelper.data.di.roomTestModule
import com.example.medihelper.data.local.DeletedHistory
import com.example.medihelper.data.local.RoomDatabase
import com.example.medihelper.data.local.SharedPref
import com.example.medihelper.domain.entities.Person
import com.example.medihelper.domain.repositories.PersonRepo
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.dsl.module

class PersonRepoImplIntegrationTest : AppAndroidTest() {

    private val testModule = module {
        single {
            get<RoomDatabase>().personDao()
        }
        single {
            SharedPref(androidContext())
        }
        single {
            DeletedHistory(androidContext())
        }
        single {
            PersonRepoImpl(
                personDao = get(),
                sharedPref = get(),
                deletedHistory = get()
            ) as PersonRepo
        }
    }

    private val personRepo: PersonRepo by inject()

    @Before
    fun before() {
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            modules(listOf(roomTestModule, testModule))
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun insert_GetEntity() {
        val person = Person(
            personId = 0,
            name = "Wojtek",
            colorId = 123,
            mainPerson = false,
            connectionKey = null
        )

        runBlocking {
            personRepo.insert(person)
        }

        val expectedResult = person.copy(personId = 1)
        val insertedPerson = runBlocking {
            personRepo.getById(1)
        }
        Truth.assertThat(insertedPerson).isEqualTo(expectedResult)
    }
}