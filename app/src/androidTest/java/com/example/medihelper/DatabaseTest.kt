package com.example.medihelper

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medihelper.localdatabase.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
abstract class DatabaseTest : KoinTest {

    private val appDatabase: AppDatabase by inject()

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            stopKoin()
        }
    }

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        startKoin {
            androidContext(context)
            modules(listOf(appModule, testDatabaseModule))
        }
    }

    @After
    fun after() {
        appDatabase.close()
        stopKoin()
    }
}