package com.maruchin.medihelper


import android.app.Activity
import android.app.Application
import com.google.firebase.FirebaseApp
import com.maruchin.medihelper.data.di.*
import com.maruchin.medihelper.data.SharedPref
import com.maruchin.medihelper.device.di.cameraModule
import com.maruchin.medihelper.device.di.deviceApiModule
import com.maruchin.medihelper.device.di.notificationModule
import com.maruchin.medihelper.presentation.di.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module


class MainApplication : Application() {

    var currActivity: Activity? = null

    private val dataModules: List<Module> by lazy {
        listOf(
            localDataModule,
            firebaseModule,
            repositoryModule
        )
    }
    private val deviceModules: List<Module> by lazy {
        listOf(
            cameraModule,
            notificationModule,
            deviceApiModule
        )
    }
    private val presentationModules: List<Module> by lazy {
        listOf(
            domainUtilsModule,
            useCasesModule,
            userUseCaseModule,
            medicineUseCaseModule,
            profileUseCaseModule,
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

