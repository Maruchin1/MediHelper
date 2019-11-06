package com.example.medihelper


import android.app.Application
import com.example.medihelper.service.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules


class MainApplication : Application() {

    private val initialDataService: InitialDataService by inject()
    private val serverApiService: ServerApiService by inject()
    private val connectedPersonDatabaseSequence by lazy {
        listOf(connectedPersonDatabaseModule, viewModelModule, serviceModule)
    }
    private val mainDatabaseSequence by lazy {
        listOf(mainDatabaseModule, viewModelModule, serviceModule)
    }

    fun switchToConnectedPersonDatabase() {
        unloadKoinModules(mainDatabaseSequence)
        loadKoinModules(connectedPersonDatabaseSequence)
    }

    fun switchToMainDatabase() {
        unloadKoinModules(connectedPersonDatabaseSequence)
        loadKoinModules(mainDatabaseSequence)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    appModule,
                    mainDatabaseModule,
                    serverApiModule,
                    viewModelModule,
                    serviceModule
                )
            )
        }
        runBlocking {
            val databaseModule = when (serverApiService.getAppMode()) {
                AppMode.CONNECTED -> connectedPersonDatabaseModule
                else -> mainDatabaseModule
            }
            loadKoinModules(databaseModule)
            initialDataService.checkInitialData()
        }
    }
}

