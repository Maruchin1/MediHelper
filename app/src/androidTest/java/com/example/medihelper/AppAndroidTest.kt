package com.example.medihelper

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4ClassRunner::class)
abstract class AppAndroidTest : KoinTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            stopKoin()
        }
    }
}