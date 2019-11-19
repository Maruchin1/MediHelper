package com.example.medihelper.serversync

import android.content.Context
import androidx.work.WorkerParameters
import com.example.medihelper.localdata.dao.PlannedMedicineDao
import com.example.medihelper.remotedata.api.ConnectedPersonApi
import com.example.medihelper.utility.NotificationUtil
import com.example.medihelper.service.ServerApiService
import org.koin.core.inject

class ConnectedPersonSyncWorker(context: Context, params: WorkerParameters) : ServerSyncWorker(context, params) {

    private val serverApiService: ServerApiService by inject()
    private val connectedPersonApi: ConnectedPersonApi by inject()
    private val notificationUtil: NotificationUtil by inject()
    private val plannedMedicineDao: PlannedMedicineDao by inject()
    private val localDatabaseDispatcher: LocalDatabaseDispatcher by inject()
    private val entityDtoMapper: EntityDtoMapper by inject()

    override suspend fun doWork(): Result {
        notificationUtil.showServerSyncNotification()
        var result = Result.failure()
        val authToken = inputData.getString(KEY_AUTH_TOKEN)

        if (authToken != null) {
            try {
                synchronizeData(authToken)
                result = Result.success()
                serverApiService.updateLastSyncTime()
            } catch (e: Exception) {
                e.printStackTrace()
                notificationUtil.showServerSyncFailNotification()
            }
        }
        notificationUtil.cancelServerSyncNotification()
        return result
    }

    private suspend fun synchronizeData(authToken: String) {
        val responseMedicineDtoList = connectedPersonApi.getMedicines(authToken)
        localDatabaseDispatcher.dispatchMedicinesChanges(responseMedicineDtoList)

        val responseMedicinePlanDtoList = connectedPersonApi.getMedicinesPlans(authToken)
        localDatabaseDispatcher.dispatchMedicinesPlansChanges(responseMedicinePlanDtoList)

        val updatedPlannedMedicineDtoList = plannedMedicineDao.getEntityListToSync().map {
            entityDtoMapper.plannedMedicineEntityToDto(it)
        }
        val responsePlannedMedicineDtoList =
            connectedPersonApi.synchronizePlannedMedicines(authToken, updatedPlannedMedicineDtoList)
        localDatabaseDispatcher.dispatchPlannedMedicinesChanges(responsePlannedMedicineDtoList)
    }
}