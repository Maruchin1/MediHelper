package com.example.medihelper.serversync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medihelper.localdatabase.repositories.*
import com.example.medihelper.remotedatabase.ConnectedPersonApi
import com.example.medihelper.services.NotificationService
import com.example.medihelper.services.SharedPrefService
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class ConnectedPersonSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params),
    KoinComponent {

    private val sharedPrefService: SharedPrefService by inject()
    private val plannedMedicineRepository: PlannedMedicineRepository by inject()
    private val connectedPersonApi: ConnectedPersonApi by inject()
    private val notificationService: NotificationService by inject()

    private val localDatabaseDispatcher: LocalDatabaseDispatcher by inject()
    private val entityDtoConverter: EntityDtoConverter by inject()

    override suspend fun doWork(): Result {
        notificationService.showServerSyncNotification()
        var result = Result.failure()
        sharedPrefService.getAuthToken()?.let { authToken ->
            try {
                synchronizeData(authToken)
                result = Result.success()
                sharedPrefService.saveLastSyncTimeLive(Date())
            } catch (e: Exception) {
                e.printStackTrace()
                notificationService.showServerSyncFailureNotification()
            }
        }
        notificationService.cancelServerSyncNotification()
        return result
    }

    private suspend fun synchronizeData(authToken: String) {
        val responseMedicineDtoList = connectedPersonApi.getMedicines(authToken)
        localDatabaseDispatcher.dispatchMedicinesChanges(responseMedicineDtoList)

        val responseMedicinePlanDtoList = connectedPersonApi.getMedicinesPlans(authToken)
        localDatabaseDispatcher.dispatchMedicinesPlansChanges(responseMedicinePlanDtoList)

        val updatedPlannedMedicineDtoList = plannedMedicineRepository.getEntityListToSync().map {
            entityDtoConverter.plannedMedicineEntityToDto(it)
        }
        val responsePlannedMedicineDtoList =
            connectedPersonApi.synchronizePlannedMedicines(authToken, updatedPlannedMedicineDtoList)
        localDatabaseDispatcher.dispatchPlannedMedicinesChanges(responsePlannedMedicineDtoList)
    }
}