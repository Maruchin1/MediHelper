package com.maruchin.medihelper


import android.app.Activity
import android.app.Application
import com.maruchin.medihelper.data.di.*
import com.maruchin.medihelper.data.local.SharedPref
import com.maruchin.medihelper.device.di.cameraModule
import com.maruchin.medihelper.device.di.deviceApiModule
import com.maruchin.medihelper.device.di.notificationModule
import com.maruchin.medihelper.presentation.di.domainUtilsModule
import com.maruchin.medihelper.presentation.di.useCasesModule
import com.maruchin.medihelper.presentation.di.utilsModule
import com.maruchin.medihelper.presentation.di.viewModelModule
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
            notificationModule,
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
        checkAppMode()
    }

    fun reloadKoin() {
        unloadKoinModules(dataModules + deviceModules + presentationModules)
        loadKoinModules(dataModules + deviceModules + presentationModules)
        checkAppMode()
    }

    private fun checkAppMode() {
        runBlocking {
            val sharedPref = SharedPref(this@MainApplication)
            val authToken = sharedPref.getAuthToken()
            val email = sharedPref.getUserEmail()

            val appModeConnected = !authToken.isNullOrEmpty() && email.isNullOrEmpty()
            if (appModeConnected) {
                unloadKoinModules(roomMainModule)
                loadKoinModules(roomConnectedModule)
            }
        }
    }
}

