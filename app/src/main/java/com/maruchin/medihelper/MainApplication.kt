package com.maruchin.medihelper


import android.app.Activity
import android.app.Application
import com.maruchin.medihelper.data.di.*
import com.maruchin.medihelper.device.di.calendarModule
import com.maruchin.medihelper.device.di.cameraModule
import com.maruchin.medihelper.presentation.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module


class MainApplication : Application() {

    var currActivity: Activity? = null

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
            calendarModule
        )
    }
    private val presentationModules: List<Module> by lazy {
        listOf(
            domainUtilsModule,
            userUseCaseModule,
            medicineUseCaseModule,
            profileUseCaseModule,
            medicinePlanUseCaseModule,
            plannedMedicineUseCaseModule,
            dateTimeUseCaseModule,
            utilsModule,
            viewModelModule
        )
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(dataModules + deviceModules + presentationModules)
        }
    }
}

