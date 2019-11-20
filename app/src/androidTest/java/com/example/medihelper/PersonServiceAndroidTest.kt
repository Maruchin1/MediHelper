package com.example.medihelper

import androidx.test.platform.app.InstrumentationRegistry
import com.example.medihelper.data.local.RoomDatabase
import com.example.medihelper.localdata.*
import com.example.medihelper.localdata.pojo.PersonEditData
import com.example.medihelper.localdata.pojo.PersonItem
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
import org.koin.test.inject

class PersonServiceAndroidTest : AppAndroidTest() {

    private val testModule = module {
        single { get<RoomDatabase>().personDao() }
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
            personId = 0,
            personName = "Wojtek",
            personColorResId = 123
        )
        runBlocking {
            personService.save(editData)
        }
        val expectedPersonItem = PersonItem(
            personId = 1,
            personName = "Wojtek",
            personColorResId = 123,
            mainPerson = false
        )
        val savedPersonItem = runBlocking {
            personService.getItem(1)
        }
        Truth.assertThat(savedPersonItem).isEqualTo(expectedPersonItem)
    }
}