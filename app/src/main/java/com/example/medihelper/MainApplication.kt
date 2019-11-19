package com.example.medihelper


import android.app.Application
import com.example.medihelper.localdata.AppSharedPref
import com.example.medihelper.localdata.AppSharedPrefImpl
import com.example.medihelper.service.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin


class MainApplication : Application() {

    private val initialDataService: InitialDataService by inject()
    private val serverApiService: ServerApiService by inject()

    override fun onCreate() {
        super.onCreate()
        val appSharedPref: AppSharedPref = AppSharedPrefImpl(this)
        val isConnected = appSharedPref.getAuthToken() != null && appSharedPref.getUserEmail() == null

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    if (isConnected) connectedRoomModule else mainRoomModule,
                    retrofitModule,
                    localDataModule,
                    remoteDataModule,
                    utilModule,
                    serviceModule,
                    viewModelModule
                )
            )
        }
        runBlocking {
            val databaseModule = when (serverApiService.getAppMode()) {
                AppMode.CONNECTED -> connectedRoomModule
                else -> mainRoomModule
            }
            loadKoinModules(databaseModule)
            initialDataService.checkInitialData()
        }
    }
}

