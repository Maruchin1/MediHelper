package com.example.medihelper

import com.example.medihelper.service.MedicineSchedulerService
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class MedicineSchedulerServiceTest : KoinTest {

    val medicineSchedulerService: MedicineSchedulerService by inject()

    @Test
    fun test() {
        startKoin { modules(listOf(appModule, repositoryModule, serviceModule)) }
    }
}