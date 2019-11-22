package com.example.medihelper


import android.app.Activity
import android.app.Application
import com.example.medihelper.data.di.*
import com.example.medihelper.device.di.cameraModule
import com.example.medihelper.device.di.deviceApiModule
import com.example.medihelper.domain.entities.AppMode
import com.example.medihelper.domain.usecases.ServerConnectionUseCases
import com.example.medihelper.presentation.di.domainUtilsModule
import com.example.medihelper.presentation.di.useCasesModule
import com.example.medihelper.presentation.di.utilsModule
import com.example.medihelper.presentation.di.viewModelModule
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module


class MainApplication : Application() {

    var currActivity: Activity? = null
    private val serverConnectionUseCases: ServerConnectionUseCases by inject()

    private val dataModules: List<Module> by lazy {
        listOf(
            roomMainModule,
            retrofitModule,
            localDataModule,
            remoteDataModule,
            syncModule,
            repositoryModule
        )
    }
    private val deviceModules: List<Module> by lazy {
        listOf(
            cameraModule,
            deviceApiModule
        )
    }
    private val presentationModules: List<Module> by lazy {
        listOf(
            domainUtilsModule,
            useCasesModule,
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
        runBlocking {
            val currAppMode = serverConnectionUseCases.getAppMode()
            if (currAppMode == AppMode.CONNECTED) {
                unloadKoinModules(roomMainModule)
                loadKoinModules(roomConnectedModule)
            }
        }
    }
}

