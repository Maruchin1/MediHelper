package com.example.medihelper.data.sync

import android.content.Context
import androidx.work.WorkerParameters
import com.example.medihelper.data.local.dao.PlannedMedicineDao
import com.example.medihelper.data.remote.api.ConnectedPersonApi
import com.example.medihelper.device.notifications.NotificationUtil
import org.koin.core.inject

class ConnectedPersonSyncWorker(context: Context, params: WorkerParameters) : ServerSyncWorker(context, params) {

    private val connectedPersonApi: ConnectedPersonApi by inject()
    private val notificationUtil: NotificationUtil = NotificationUtil(context)
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