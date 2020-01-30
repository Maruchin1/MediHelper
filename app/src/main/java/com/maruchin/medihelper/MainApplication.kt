package com.maruchin.medihelper


import android.app.Activity
import android.app.Application
import com.maruchin.medihelper.data.di.*
import com.maruchin.medihelper.device.di.*
import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.device.DeviceUpdates
import com.maruchin.medihelper.domain.di.*
import com.maruchin.medihelper.presentation.di.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

class MainApplication : Application() {

    var currActivity: Activity? = null

    private val domainModules: List<Module> by lazy {
        listOf(
            domainUtilsModule,
            userUseCaseModule,
            medicineUseCaseModule,
            profileUseCaseModule,
            planUseCaseModule,
            plannedMedicineUseCaseModule,
            settingsUseCasesModule,
            savedTypesUseCaseModule
        )
    }
    private val dataModules: List<Module> by lazy {
        listOf(
            localDataModule,
            firebaseModule,
            repositoryModule,
            mapperModule
        )
    }
    private val deviceModules: List<Module> by lazy {
        listOf(
            cameraModule,
            calendarModule,
            notificationsModule,
            ringtoneModule,
            updatesModule
        )
    }
    private val presentationModules: List<Module> by lazy {
        listOf(
            presentationUtilsModule,
            presentationFeaturesUtilsModule,
            viewModelModule
        )
    }
    private val reminder: DeviceReminder by inject()
    private val updates: DeviceUpdates by inject()

    fun reloadDependencies() {
        stopKoin()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(domainModules + dataModules + deviceModules + presentationModules)
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(domainModules + dataModules + deviceModules + presentationModules)
        }
        GlobalScope.launch {
            reminder.setupPlannedMedicinesReminders()
            updates.setupContinuousPlansUpdates()
        }
    }
}

