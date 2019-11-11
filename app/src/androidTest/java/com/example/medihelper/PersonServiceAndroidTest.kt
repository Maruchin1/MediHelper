package com.example.medihelper

import androidx.test.platform.app.InstrumentationRegistry
import com.example.medihelper.localdatabase.*
import com.example.medihelper.localdatabase.pojo.PersonEditData
import com.example.medihelper.localdatabase.pojo.PersonItem
import com.example.medihelper.service.PersonService
import com.example.medihelper.service.PersonServiceImpl
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class PersonServiceAndroidTest : AppAndroidTest() {

    private val testModule = module {
        single { get<AppDatabase>().personDao() }
        single<AppSharedPref> { AppSharedPrefImpl(androidContext()) }
        single<DeletedHistory> { DeletedHistoryImpl(androidContext()) }
        single<PersonService> { PersonServiceImpl(get(), get(), get()) }
    }

    private val personService: PersonService by inject()

    @Before
    fun before() {
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            modules(listOf(testRoomModule, testModule))
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun save_New_GetEntity() {
        val editData = PersonEditData(
            personID = 0,
            personName = "Wojtek",
            personColorResID = 123
        )
        runBlocking {
            personService.save(editData)
        }
        val expectedPersonItem = PersonItem(
            personID = 1,
            personName = "Wojtek",
            personColorResID = 123,
            mainPerson = false
        )
        val savedPersonItem = runBlocking {
            personService.getItem(1)
        }
        Truth.assertThat(savedPersonItem).isEqualTo(expectedPersonItem)
    }
}